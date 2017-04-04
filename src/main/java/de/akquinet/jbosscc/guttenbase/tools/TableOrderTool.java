package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

import java.sql.SQLException;
import java.util.*;

/**
 * Order tables by foreign key constraints, i.e. the foreign keys of a database schema spawn an directed (possibly cyclic!) graph
 * of dependencies. The tool tries to create of sequential order either in top-down (starting at the root nodes) or bottom-up
 * (starting at the leaves) manner. <br>
 * If there are cycles in the dependencies, we choose the node with the fewest incoming/outgoing edges.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public class TableOrderTool
{
  public List<TableMetaData> getOrderedTables(final List<TableMetaData> tableMetaData, final boolean topDown) throws SQLException
  {
    final Map<String, TableNode> tableNodes = createGraph(tableMetaData);

    return orderTables(tableNodes, topDown);
  }

  private List<TableMetaData> orderTables(final Map<String, TableNode> tableNodes, final boolean topDown) throws SQLException
  {
    final List<TableMetaData> result = new ArrayList<>();

    while (!tableNodes.isEmpty())
    {
      final TableNode tableNode = findMatchingNode(new ArrayList<>(tableNodes.values()), topDown);

      for (final TableNode referencingTable : tableNode.getReferencedByTables())
      {
        referencingTable.removeReferencedTable(tableNode);
      }

      for (final TableNode referencedTable : tableNode.getReferencedTables())
      {
        referencedTable.removeReferencedByTable(tableNode);
      }

      result.add(tableNode.getTableMetaData());
      tableNodes.remove(tableNode.getTableMetaData().getTableName().toUpperCase());
    }

    return result;
  }

  private TableNode findMatchingNode(final List<TableNode> tableNodes, final boolean topDown)
  {
    tableNodes.sort((tn1, tn2) -> {
      if (topDown) {
        return tn1.getReferencedTables().size() - tn2.getReferencedTables().size();
      } else {
        return tn1.getReferencedByTables().size() - tn2.getReferencedByTables().size();
      }
    });

    return tableNodes.get(0);
  }

  private Map<String, TableNode> createGraph(final List<TableMetaData> tableMetaData)
  {
    final Map<String, TableNode> tableNodes = new LinkedHashMap<>();

    for (final TableMetaData table : tableMetaData)
    {
      final List<ForeignKeyMetaData> importedForeignKeys = table.getImportedForeignKeys();
      final TableNode tableNode = getTableNode(tableNodes, table);

      for (final ForeignKeyMetaData foreignKeyMetaData : importedForeignKeys)
      {
        final TableNode referencingTable = getTableNode(tableNodes, foreignKeyMetaData.getReferencingColumn().getTableMetaData());
        final TableNode referencedTable = getTableNode(tableNodes, foreignKeyMetaData.getReferencedColumn().getTableMetaData());

        assert tableNode.equals(referencingTable);

        referencingTable.addReferencedTable(referencedTable);
        referencedTable.addReferencedByTable(referencingTable);
      }
    }

    return tableNodes;
  }

  private TableNode getTableNode(final Map<String, TableNode> tableNodes, final TableMetaData table)
  {
    final String tableName = table.getTableName().toUpperCase();

    if (tableNodes.containsKey(tableName))
    {
      return tableNodes.get(tableName);
    }
    else
    {
      final TableNode result = new TableNode(table);
      tableNodes.put(tableName, result);
      return result;
    }
  }

  private static class TableNode
  {
    private final TableMetaData _tableMetaData;
    private final List<TableNode> _referencedTables = new ArrayList<>();
    private final List<TableNode> _referencedByTables = new ArrayList<>();

    public TableNode(final TableMetaData tableMetaData)
    {
      _tableMetaData = tableMetaData;
    }

    public void addReferencedTable(final TableNode tableMetaData)
    {
      _referencedTables.add(tableMetaData);
    }

    public void removeReferencedTable(final TableNode tableMetaData)
    {
      _referencedTables.remove(tableMetaData);
    }

    public void addReferencedByTable(final TableNode tableMetaData)
    {
      _referencedByTables.add(tableMetaData);
    }

    public void removeReferencedByTable(final TableNode tableMetaData)
    {
      _referencedByTables.remove(tableMetaData);
    }

    public List<TableNode> getReferencedTables()
    {
      return _referencedTables;
    }

    public List<TableNode> getReferencedByTables()
    {
      return _referencedByTables;
    }

    public TableMetaData getTableMetaData()
    {
      return _tableMetaData;
    }

    @Override
    public int hashCode()
    {
      return _tableMetaData.hashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
      final TableNode that = (TableNode) obj;
      return this.getTableMetaData().equals(that.getTableMetaData());
    }

    @Override
    public String toString()
    {
      return _tableMetaData.getTableName() + "::referencedTables:"
              + toString(getReferencedTables())
              + ", referencedByTables: "
              + toString(getReferencedByTables());
    }

    private static String toString(final List<TableNode> referencedTables)
    {
      List<String> result = new ArrayList<>();

      for (TableNode tableNode : referencedTables)
      {
        result.add(tableNode.getTableMetaData().getTableName());
      }

      return result.toString();
    }
  }
}
