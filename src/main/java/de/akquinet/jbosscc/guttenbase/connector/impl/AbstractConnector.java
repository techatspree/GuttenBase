package de.akquinet.jbosscc.guttenbase.connector.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import de.akquinet.jbosscc.guttenbase.configuration.SourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.TargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.connector.Connector;
import de.akquinet.jbosscc.guttenbase.connector.ConnectorInfo;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.DatabaseMetaDataInspectorTool;

/**
 * Default connector implementation.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public abstract class AbstractConnector implements Connector {
  protected static final Logger LOG = Logger.getLogger(AbstractConnector.class);

  protected transient Connection _connection;

  protected final ConnectorRepository _connectorRepository;
  protected final String _connectorId;
  protected final ConnectorInfo _connectionInfo;

  public AbstractConnector(final ConnectorRepository connectorRepository, final String connectorId, final ConnectorInfo connectionInfo) {
    assert connectionInfo != null : "connectionInfo != null";
    assert connectorId != null : "connectorId != null";
    assert connectorRepository != null : "connectorRepository != null";

    _connectionInfo = connectionInfo;
    _connectorRepository = connectorRepository;
    _connectorId = connectorId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void closeConnection() throws SQLException {
    try {
      if (_connection != null) {
        if (!_connection.isClosed()) {
          final TargetDatabaseConfiguration targetDatabaseConfiguration = _connectorRepository.getTargetDatabaseConfiguration(_connectorId);

          if (!_connection.getAutoCommit() && targetDatabaseConfiguration.isMayCommit()) {
            _connection.commit();
          }

          _connection.close();
        }
      }
    } catch (final SQLException e) {
      LOG.warn("Closing connection failed", e);
      throw e;
    } finally {
      _connection = null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DatabaseMetaData retrieveDatabaseMetaData() throws SQLException {
    final DatabaseMetaDataInspectorTool tableMetaDataInspector = new DatabaseMetaDataInspectorTool(_connectorRepository, _connectorId);
    final Connection connection = openConnection();
    final SourceDatabaseConfiguration sourceDatabaseConfiguration = _connectorRepository.getSourceDatabaseConfiguration(_connectorId);

    sourceDatabaseConfiguration.initializeSourceConnection(connection, _connectorId);
    final DatabaseMetaData databaseMetaData = tableMetaDataInspector.getDatabaseMetaData(connection);
    sourceDatabaseConfiguration.finalizeSourceConnection(connection, _connectorId);
    closeConnection();
    return databaseMetaData;
  }
}