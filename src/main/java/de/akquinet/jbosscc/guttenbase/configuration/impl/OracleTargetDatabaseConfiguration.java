package de.akquinet.jbosscc.guttenbase.configuration.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.akquinet.jbosscc.guttenbase.hints.TableNameMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;

/**
 * Implementation for Oracle data base.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @Uses-Hint {@link TableNameMapperHint}
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
      String schema = _connectorRepository.getConnectionInfo(connectorId).getSchema();
      /* I want to disable all constraints in tables that reference the tables that I will update. */
      final List<Map<String, Object>> foreignKeyNames = new ScriptExecutorTool(_connectorRepository).executeQuery(connectorId,
          "SELECT DISTINCT AC.OWNER, AC.TABLE_NAME, AC.CONSTRAINT_NAME FROM ALL_CONSTRAINTS AC, ALL_CONS_COLUMNS ACC WHERE AC.CONSTRAINT_TYPE = 'R' AND ACC.TABLE_NAME IN (" +
               tablesList + ") AND ACC.OWNER = '" + schema + "' AND ACC.CONSTRAINT_NAME = AC.R_CONSTRAINT_NAME AND ACC.OWNER = AC.R_OWNER");

      // memorize any problems that occur during constraint handling
      StringBuilder problems = new StringBuilder();

      for (final Map<String, Object> fkMap : foreignKeyNames) {
        final String tableName = fkMap.get("TABLE_NAME").toString();
        final String constraintName = fkMap.get("CONSTRAINT_NAME").toString();
        final String owner = fkMap.get("OWNER").toString();

        try {
          executeSQL(connection, "ALTER TABLE " + owner + "." + tableName + (enable ? " ENABLE " : " DISABLE ") + "CONSTRAINT " + constraintName);
        } catch (SQLException e) {
          // when there is an error with on constraint memorize it, but continue with other constraints
          StringBuilder problem = new StringBuilder();
          problem.append(owner).append(".").append(tableName).append(enable ? " ENABLE " : " DISABLE ").append("constraint ").append(constraintName);
          LOG.error("unable to handle constraint: " + problem, e);
          problems.append(problem).append(": ").append(e.getMessage());
        }
      }

      if(problems.length() > 0) {
        // if there has been a problem with any constraint, now throw the exception
        throw new SQLException("constraint problems occurred: " + problems.toString());
      }

    }
  }

  private String createTablesList(final List<TableMetaData> tableMetaDatas) {
    final StringBuilder tablesBuilder = new StringBuilder();

    for (final TableMetaData tableMetaData : tableMetaDatas) {
      tablesBuilder.append("'" + tableMetaData.getTableName() + "'" + ", ");
    }

    if (tablesBuilder.length() > 2) {
      tablesBuilder.setLength(tablesBuilder.length() - 2);
    }

    return tablesBuilder.toString();
  }
}
