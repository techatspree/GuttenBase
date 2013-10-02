package de.akquinet.jbosscc.guttenbase.configuration.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
      final List<Map<String, Object>> foreignKeyNames = new ScriptExecutorTool(_connectorRepository).executeQuery(connectorId,
          "SELECT TABLE_NAME, CONSTRAINT_NAME FROM USER_CONSTRAINTS WHERE CONSTRAINT_TYPE = 'R' AND TABLE_NAME IN (" + tablesList + ")");

      for (final Map<String, Object> fkMap : foreignKeyNames) {
        final String tableName = fkMap.get("TABLE_NAME").toString();
        final String constraintName = fkMap.get("CONSTRAINT_NAME").toString();

        executeSQL(connection, "ALTER TABLE " + tableName + (enable ? " ENABLE " : " DISABLE ") + "CONSTRAINT " + constraintName);
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
