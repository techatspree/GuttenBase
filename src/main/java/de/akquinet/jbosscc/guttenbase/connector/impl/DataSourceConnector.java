package de.akquinet.jbosscc.guttenbase.connector.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Connection info via data source and optional user/password.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DataSourceConnector extends AbstractConnector {
  protected static final Logger LOG = Logger.getLogger(DataSourceConnector.class);

  private final DataSourceConnectorInfo _dataSourceConnectionInfo;

  public DataSourceConnector(final ConnectorRepository connectorRepository, final String connectorId,
      final DataSourceConnectorInfo dataSourceConnectionInfo) {
    super(connectorRepository, connectorId, dataSourceConnectionInfo);

    assert dataSourceConnectionInfo != null : "dataSourceConnectionInfo != null";
    _dataSourceConnectionInfo = dataSourceConnectionInfo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Connection openConnection() throws SQLException {
    if (_connection == null || _connection.isClosed()) {
      if (_dataSourceConnectionInfo.getUser() != null && _dataSourceConnectionInfo.getPassword() != null) {
        _connection = _dataSourceConnectionInfo.getDataSource().getConnection(_dataSourceConnectionInfo.getUser(),
            _dataSourceConnectionInfo.getPassword());
      } else {
        _connection = _dataSourceConnectionInfo.getDataSource().getConnection();
      }
    }

    return _connection;
  }
}