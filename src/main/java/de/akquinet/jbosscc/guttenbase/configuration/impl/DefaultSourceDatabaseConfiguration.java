package de.akquinet.jbosscc.guttenbase.configuration.impl;

import de.akquinet.jbosscc.guttenbase.configuration.SourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * (Almost) empty implementation
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DefaultSourceDatabaseConfiguration extends AbstractDatabaseConfiguration implements SourceDatabaseConfiguration {
  public DefaultSourceDatabaseConfiguration(final ConnectorRepository connectorRepository) {
    super(connectorRepository);
  }

  /**
   * Connection is set read only and autocommit is false.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void initializeSourceConnection(final Connection connection, final String connectorId) throws SQLException {
    if (connection.getAutoCommit()) {
      connection.setAutoCommit(false);
    }

    if (!connection.isReadOnly()) {
      connection.setReadOnly(true);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void finalizeSourceConnection(final Connection connection, final String connectorId) throws SQLException {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void beforeSelect(final Connection connection, final String connectorId, final TableMetaData table) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void afterSelect(final Connection connection, final String connectorId, final TableMetaData table) {
  }
}
