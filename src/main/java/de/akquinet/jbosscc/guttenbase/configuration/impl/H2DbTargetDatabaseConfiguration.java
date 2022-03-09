package de.akquinet.jbosscc.guttenbase.configuration.impl;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Implementation for H2DB data base.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class H2DbTargetDatabaseConfiguration extends DefaultTargetDatabaseConfiguration {
  public H2DbTargetDatabaseConfiguration(final ConnectorRepository connectorRepository) {
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
    final String referentialIntegrity = enable ? "TRUE" : "FALSE";

    executeSQL(connection, "SET REFERENTIAL_INTEGRITY " + referentialIntegrity);
  }
}
