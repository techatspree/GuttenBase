package de.akquinet.jbosscc.guttenbase.connector.impl;

import de.akquinet.jbosscc.guttenbase.connector.Connector;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Connection info via explicit URL and driver.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class URLConnectorInfoImpl implements URLConnectorInfo {
  private static final long serialVersionUID = 1L;

  private final String _url;
  private final String _user;
  private final String _password;
  private final String _driver;
  private final String _schema;
  private final DatabaseType _databaseType;

  public URLConnectorInfoImpl(final String url, final String user, final String password, final String driver, final String schema,
                              final DatabaseType databaseType) {
    assert schema != null : "schema != null";
    assert driver != null : "driver != null";
    assert password != null : "password != null";
    assert user != null : "user != null";
    assert url != null : "url != null";
    assert databaseType != null : "databaseType != null";

    _url = url;
    _user = user;
    _password = password;
    _driver = driver;
    _schema = schema;
    _databaseType = databaseType;
  }

  /**
   * {@inheritDoc}
   */
  public String getUrl() {
    return _url;
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

  public String getDriver() {
    return _driver;
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
    return new URLConnector(connectorRepository, connectorId, this);
  }
}