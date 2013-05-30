package de.akquinet.jbosscc.guttenbase.configuration.impl;

import java.sql.Connection;
import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Implementation for HSQLDB data base.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class HsqldbTargetDatabaseConfiguration extends DefaultTargetDatabaseConfiguration {
  public HsqldbTargetDatabaseConfiguration(final ConnectorRepository connectorRepository) {
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

    setReferentialIntegrity(connection, false, _connectorRepository.getDatabaseMetaData(connectorId));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void finalizeTargetConnection(final Connection connection, final String connectorId) throws SQLException {
    setReferentialIntegrity(connection, true, _connectorRepository.getDatabaseMetaData(connectorId));
  }

  private void setReferentialIntegrity(final Connection connection, final boolean enable, final DatabaseMetaData databaseMetaData)
      throws SQLException {
    final int databaseMajorVersion = databaseMetaData.getMajorVersion();
    final String referentialIntegrity = enable ? "TRUE" : "FALSE";

    final String command = databaseMajorVersion < 2 ? "SET REFERENTIAL_INTEGRITY " : "SET DATABASE REFERENTIAL INTEGRITY ";
    executeSQL(connection, command + referentialIntegrity);
  }
}
