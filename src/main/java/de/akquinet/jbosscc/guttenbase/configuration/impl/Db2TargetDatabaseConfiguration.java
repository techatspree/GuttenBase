package de.akquinet.jbosscc.guttenbase.configuration.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.hints.TableNameMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

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
  public Db2TargetDatabaseConfiguration(final ConnectorRepository connectorRepository)
  {
    super(connectorRepository);
  }

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

    disableTableForeignKeys(connection, connectorId, getTableMetaData(connectorId));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void finalizeTargetConnection(final Connection connection, final String connectorId) throws SQLException
  {
    enableTableForeignKeys(connection, connectorId, getTableMetaData(connectorId));
  }

  private List<TableMetaData> getTableMetaData(final String connectorId) throws SQLException
  {
    return _connectorRepository.getDatabaseMetaData(connectorId).getTableMetaData();
  }

  private void disableTableForeignKeys(final Connection connection, final String connectorId,
      final List<TableMetaData> tableMetaData) throws SQLException
  {
    setTableForeignKeys(connection, connectorId, tableMetaData, false);
  }

  private void enableTableForeignKeys(final Connection connection, final String connectorId,
      final List<TableMetaData> tableMetaData) throws SQLException
  {
    setTableForeignKeys(connection, connectorId, tableMetaData, true);
  }

  private void setTableForeignKeys(final Connection connection, final String connectorId,
      final List<TableMetaData> tableMetaDatas, final boolean enable) throws SQLException
  {
    final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(connectorId, TableNameMapper.class).getValue();

    for (final TableMetaData tableMetaData : tableMetaDatas)
    {
      final String tableName = tableNameMapper.mapTableName(tableMetaData);

      for (final ForeignKeyMetaData foreignKey : tableMetaData.getForeignKeys())
      {
        executeSQL(connection, "ALTER TABLE " + tableName
            + " ALTER FOREIGN KEY "
            + foreignKey.getForeignKeyName()
            + (enable ? " ENFORCED" : " NOT ENFORCED"));
      }
    }
  }
}
