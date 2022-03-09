package de.akquinet.jbosscc.guttenbase.connector.impl;

import de.akquinet.jbosscc.guttenbase.connector.Connector;
import de.akquinet.jbosscc.guttenbase.connector.ConnectorInfo;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

import javax.sql.DataSource;

/**
 * Connector info via data source with optional user/password. To be used when running in an application server.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DataSourceConnectorInfo implements ConnectorInfo {
  private static final long serialVersionUID = 1L;

  private final DataSource _dataSource;
  private final String _user;
  private final String _password;
  private final String _schema;
  private final DatabaseType _databaseType;

  public DataSourceConnectorInfo(final DataSource dataSource, final String user, final String password, final String schema,
                                 final DatabaseType databaseType) {
    assert schema != null : "schema != null";
    assert dataSource != null : "dataSource != null";
    assert databaseType != null : "databaseType != null";

    _dataSource = dataSource;
    _user = user;
    _password = password;
    _schema = schema;
    _databaseType = databaseType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUser() {
    return _user;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPassword() {
    return _password;
  }

  public DataSource getDataSource() {
    return _dataSource;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSchema() {
    return _schema;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DatabaseType getDatabaseType() {
    return _databaseType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Connector createConnector(final ConnectorRepository connectorRepository, final String connectorId) {
    return new DataSourceConnector(connectorRepository, connectorId, this);
  }
}