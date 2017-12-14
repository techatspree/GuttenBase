package de.akquinet.jbosscc.guttenbase.configuration.impl;

import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Implementation for Oracle data base.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class OracleTargetDatabaseConfiguration extends DefaultTargetDatabaseConfiguration {
  private static final Logger LOG = Logger.getLogger(OracleTargetDatabaseConfiguration.class);

  public OracleTargetDatabaseConfiguration(final ConnectorRepository connectorRepository) {
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
    final String tablesList = createTablesList(tableMetaDatas);

    if (!"".equals(tablesList)) {
      final String schema = _connectorRepository.getConnectionInfo(connectorId).getSchema();

      /* I want to disable all constraints in tables that reference the tables that I will update. */
      final List<Map<String, Object>> foreignKeyNames = new ScriptExecutorTool(_connectorRepository).executeQuery(connectorId,
          "SELECT DISTINCT AC.OWNER, AC.TABLE_NAME, AC.CONSTRAINT_NAME FROM ALL_CONSTRAINTS AC, ALL_CONS_COLUMNS ACC "
              + "WHERE AC.CONSTRAINT_TYPE = 'R' "//
              + "AND ACC.TABLE_NAME IN (" + tablesList + ") "//
              + "AND ACC.OWNER = '" + schema + "' "//
              + "AND ACC.CONSTRAINT_NAME = AC.R_CONSTRAINT_NAME "//
              + "AND ACC.OWNER = AC.R_OWNER");

      // memorize any problems that occur during constraint handling
      final StringBuilder problems = new StringBuilder();

      for (final Map<String, Object> fkMap : foreignKeyNames) {
        final String tableName = fkMap.get("TABLE_NAME").toString();
        final String constraintName = fkMap.get("CONSTRAINT_NAME").toString();
        final String owner = fkMap.get("OWNER").toString();
        final String sql = "ALTER TABLE " + owner + "." + tableName + (enable ? " ENABLE " : " DISABLE ") + "CONSTRAINT " + constraintName;

        try {
          executeSQL(connection, sql);
        } catch (final SQLException e) {
          LOG.error("Unable to handle constraint: " + sql, e);
          problems.append("Unable to handle constraint: ").append(sql).append("->").append(e.getMessage()).append(":").append(e.getSQLState());
        }
      }

      if (problems.length() > 0) {
        // if there has been a problem with any constraint, now throw the exception
        throw new SQLException("Constraint problems occurred: " + problems);
      }

    }
  }

  private static String createTablesList(final List<TableMetaData> tableMetaDatas) {
    final StringBuilder tablesBuilder = new StringBuilder();

    for (final TableMetaData tableMetaData : tableMetaDatas) {
      tablesBuilder.append("'").append(tableMetaData.getTableName()).append("'").append(", ");
    }

    if (tablesBuilder.length() > 2) {
      tablesBuilder.setLength(tablesBuilder.length() - 2);
    }

    return tablesBuilder.toString();
  }
}
