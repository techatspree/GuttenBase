package de.akquinet.jbosscc.guttenbase.configuration.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.akquinet.jbosscc.guttenbase.hints.TableNameMapperHint;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;

/**
 * Implementation for IBM DB2 data base.
 * 
 * @see http://stackoverflow.com/questions/421518/is-there-a-way-to-enable-disable-constraints-in-db2-v7
 *      <p>
 *      &copy; 2012 akquinet tech@spree
 *      </p>
 * @Uses-Hint {@link TableNameMapperHint}
 * @author M. Dahm
 */
public class Db2TargetDatabaseConfiguration extends DefaultTargetDatabaseConfiguration
{
  private Map<String, String> _constraintsOfTable;
  private String _schema;

  public Db2TargetDatabaseConfiguration(final ConnectorRepository connectorRepository)
  {
    super(connectorRepository);
  }

  //  @Override
  //  public void beforeInsert(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException
  //  {
  //    final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(connectorId, TableNameMapper.class).getValue();
  //
  //    final String tableName = tableNameMapper.mapTableName(table);
  //
  //    executeSQL(connection, "SET INTEGRITY FOR " + tableName + " OFF");
  //    executeSQL(connection, "SET INTEGRITY FOR " + tableName + " FOREIGN KEY IMMEDIATE UNCHECKED");
  //  }
  //
  //  @Override
  //  public void afterInsert(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException
  //  {
  //    final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(connectorId, TableNameMapper.class).getValue();
  //
  //    final String tableName = tableNameMapper.mapTableName(table);
  //
  //    executeSQL(connection, "SET INTEGRITY FOR " + tableName + " IMMEDIATE CHECKED");
  //  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeTargetConnection(final Connection connection, final String connectorId) throws SQLException
  {
    if (connection.getAutoCommit())
    {
      connection.setAutoCommit(false);
    }

    final DatabaseMetaData databaseMetaData = _connectorRepository.getDatabaseMetaData(connectorId);
    _schema = databaseMetaData.getSchema();
    _constraintsOfTable = loadConstraints(connection);

    setTableForeignKeys(connection, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void finalizeTargetConnection(final Connection connection, final String connectorId) throws SQLException
  {
    setTableForeignKeys(connection, true);
  }

  private void setTableForeignKeys(final Connection connection, final boolean enable) throws SQLException
  {
    for (final Entry<String, String> entry : _constraintsOfTable.entrySet())
    {
      final String constraintName = entry.getKey();
      final String tableName = entry.getValue();

      executeSQL(connection, "ALTER TABLE " + _schema
          + "."
          + tableName
          + " ALTER FOREIGN KEY "
          + constraintName
          + (enable ? " ENFORCED" : " NOT ENFORCED"));
    }
  }

  private Map<String, String> loadConstraints(final Connection connection) throws SQLException
  {
    final HashMap<String, String> result = new HashMap<String, String>();
    final ScriptExecutorTool scriptExecutorTool = new ScriptExecutorTool(_connectorRepository);
    final List<Map<String, Object>> queryResult = scriptExecutorTool.executeQuery(connection,
        "SELECT CONSTNAME, TABNAME FROM SYSCAT.TABCONST WHERE TABSCHEMA='" + _schema + "' AND TYPE='F' ORDER BY TABNAME");

    for (final Map<String, Object> map : queryResult)
    {
      final String constraintName = String.valueOf(map.get("CONSTNAME"));
      final String tableName = String.valueOf(map.get("TABNAME"));
      result.put(constraintName, tableName);
    }

    return result;
  }
}
