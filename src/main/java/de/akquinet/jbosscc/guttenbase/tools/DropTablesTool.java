package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.connector.ConnectorInfo;
import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Will drop tables in given schema. USE WITH CARE!
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 * @gb.UsesHint {@link TableOrderHint} to determine order of tables
 */
public class DropTablesTool
{
  final ConnectorRepository _connectorRepository;

  public DropTablesTool(final ConnectorRepository connectorRepository)
  {
    assert connectorRepository != null : "connectorRepository != null";
    _connectorRepository = connectorRepository;
  }

  public List<String> createDropForeignKeyStatements(final String connectorId) throws SQLException
  {
    final List<TableMetaData> tableMetaData = new TableOrderTool().getOrderedTables(
            TableOrderHint.getSortedTables(_connectorRepository, connectorId), false);
    final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(connectorId, TableNameMapper.class).getValue();
    final List<String> statements = new ArrayList<String>();
    final ConnectorInfo connectionInfo = _connectorRepository.getConnectionInfo(connectorId);
    String constraintClause;

    switch (connectionInfo.getDatabaseType())
    {
      case MYSQL:
        constraintClause = " FOREIGN KEY ";
        break;
      default:
        constraintClause = " CONSTRAINT ";
        break;
    }

    for (final TableMetaData table : tableMetaData)
    {
      for (final ForeignKeyMetaData foreignKey : table.getImportedForeignKeys())
      {
        statements.add("ALTER TABLE " + tableNameMapper.mapTableName(table)
                + " DROP"
                + constraintClause
                + foreignKey.getForeignKeyName()
                + ";");
      }
    }

    return statements;
  }

  public List<String> createDropIndexesStatements(final String connectorId) throws SQLException
  {
    final List<TableMetaData> tableMetaData = new TableOrderTool().getOrderedTables(
            TableOrderHint.getSortedTables(_connectorRepository, connectorId), false);
    final List<String> statements = new ArrayList<String>();

    for (final TableMetaData table : tableMetaData)
    {
      String schema = table.getDatabaseMetaData().getSchema();

      if (!"".equals(schema))
      {
        schema += ".";
      }

      for (final IndexMetaData index : table.getIndexes())
      {
        if (!index.isPrimaryKeyIndex())
        {
          statements.add("DROP INDEX " + schema + index.getIndexName() + ";");
        }
      }
    }

    return statements;
  }

  public List<String> createDropTableStatements(final String connectorId) throws SQLException
  {
    final List<TableMetaData> tableMetaData = new TableOrderTool().getOrderedTables(
            TableOrderHint.getSortedTables(_connectorRepository, connectorId), false);
    final List<String> statements = new ArrayList<String>();
    final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(connectorId, TableNameMapper.class).getValue();

    for (final TableMetaData table : tableMetaData)
    {
      statements.add("DROP TABLE " + tableNameMapper.mapTableName(table) + ";");
    }

    return statements;
  }

  public void dropTables(final String targetId) throws SQLException
  {
    new ScriptExecutorTool(_connectorRepository).executeScript(targetId, true, false, createDropTableStatements(targetId));
  }

  public void dropIndexes(final String targetId) throws SQLException
  {
    new ScriptExecutorTool(_connectorRepository).executeScript(targetId, true, true, createDropIndexesStatements(targetId));
  }

  public void dropForeignKeys(final String targetId) throws SQLException
  {
    new ScriptExecutorTool(_connectorRepository).executeScript(targetId, true, true, createDropForeignKeyStatements(targetId));
  }
}
