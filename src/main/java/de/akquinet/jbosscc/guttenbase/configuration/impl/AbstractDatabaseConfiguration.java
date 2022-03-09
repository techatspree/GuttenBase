package de.akquinet.jbosscc.guttenbase.configuration.impl;

import de.akquinet.jbosscc.guttenbase.configuration.DatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Abstract base implementation of data base configuration.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public abstract class AbstractDatabaseConfiguration implements DatabaseConfiguration {
  private static final Logger LOG = Logger.getLogger(DatabaseConfiguration.class);

  protected final ConnectorRepository _connectorRepository;

  public AbstractDatabaseConfiguration(final ConnectorRepository connectorRepository) {
    assert connectorRepository != null : "connectorRepository != null";

    _connectorRepository = connectorRepository;
  }

  /**
   * Execute single statement.
   */
  protected void executeSQL(final Connection connection, final String sql) throws SQLException {
    LOG.debug("Executing: " + sql);

    final Statement statement = connection.createStatement();
    statement.execute(sql);
    statement.close();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void beforeTableCopy(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void afterTableCopy(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException {
  }
}
