package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.mapping.TableOrderComparatorFactory;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.AbstractTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

/**
 * Determine order of tables during copying/comparison.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 *
 * Hint is used by {@link SchemaComparatorTool} to determine table order
 * Hint is used by {@link AbstractTableCopyTool} to determine table order
 * Hint is used by {@link CheckEqualTableDataTool} to determine table order
 * @author M. Dahm
 */
@SuppressWarnings("deprecation")
public abstract class TableOrderHint implements ConnectorHint<TableOrderComparatorFactory>
{
  @Override
  public Class<TableOrderComparatorFactory> getConnectorHintType()
  {
    return TableOrderComparatorFactory.class;
  }

  /**
   * Helper method
   */
  public static List<TableMetaData> getSortedTables(final ConnectorRepository connectorRepository, final String connectorId)
      throws SQLException
  {
    final DatabaseMetaData databaseMetaData = connectorRepository.getDatabaseMetaData(connectorId);
    final Comparator<TableMetaData> comparator = connectorRepository
        .getConnectorHint(connectorId, TableOrderComparatorFactory.class).getValue().createComparator();
    final List<TableMetaData> tables = databaseMetaData.getTableMetaData();
    tables.sort(comparator);

    return tables;
  }
}
