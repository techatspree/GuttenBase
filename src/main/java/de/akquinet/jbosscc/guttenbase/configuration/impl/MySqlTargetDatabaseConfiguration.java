package de.akquinet.jbosscc.guttenbase.configuration.impl;

import java.sql.Connection;
import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Implementation for MYSQL data base.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class MySqlTargetDatabaseConfiguration extends DefaultTargetDatabaseConfiguration {
  public MySqlTargetDatabaseConfiguration(final ConnectorRepository connectorRepository) {
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

    setReferentialIntegrity(connection, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void finalizeTargetConnection(final Connection connection, final String connectorId) throws SQLException {
    setReferentialIntegrity(connection, true);

  }

  private void setReferentialIntegrity(final Connection connection, final boolean enable) throws SQLException {
    executeSQL(connection, "SET FOREIGN_KEY_CHECKS = " + (enable ? "1" : "0") + ";");
  }
}
