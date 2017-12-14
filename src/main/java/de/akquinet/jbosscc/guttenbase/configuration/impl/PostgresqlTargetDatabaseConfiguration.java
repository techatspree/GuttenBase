package de.akquinet.jbosscc.guttenbase.configuration.impl;

import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Implementation for PostgreSQL data base.
 * <p></p>
 * Running ANALYZE after insertions is recommended: http://www.postgresql.org/docs/7.4/static/populate.html
 * <p></p>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class PostgresqlTargetDatabaseConfiguration extends DefaultTargetDatabaseConfiguration {
  public PostgresqlTargetDatabaseConfiguration(final ConnectorRepository connectorRepository) {
    super(connectorRepository);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeTargetConnection(final Connection connection, final String connectorId) throws SQLException {
    if (connection.getAutoCommit()) {
      connection.setAutoCommit(false);
    }

    setReferentialIntegrity(connection, connectorId, getTableMetaData(connectorId), false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void finalizeTargetConnection(final Connection connection, final String connectorId) throws SQLException {
    setReferentialIntegrity(connection, connectorId, getTableMetaData(connectorId), true);
  }

  private List<TableMetaData> getTableMetaData(final String connectorId) throws SQLException {
    return TableOrderHint.getSortedTables(_connectorRepository, connectorId);
  }

  private void setReferentialIntegrity(final Connection connection, final String connectorId, final List<TableMetaData> tableMetaDatas,
                                       final boolean enable) throws SQLException {
    for (final TableMetaData tableMetaData : tableMetaDatas) {
      final TableMapper tableNameMapper = _connectorRepository.getConnectorHint(connectorId, TableMapper.class).getValue();
      final String tableName = tableNameMapper.fullyQualifiedTableName(tableMetaData, tableMetaData.getDatabaseMetaData());
      executeSQL(connection, "ALTER TABLE " + tableName + (enable ? " ENABLE " : " DISABLE ") + "TRIGGER ALL;");
    }
  }
}
