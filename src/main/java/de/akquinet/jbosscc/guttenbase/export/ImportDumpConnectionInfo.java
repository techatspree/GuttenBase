package de.akquinet.jbosscc.guttenbase.export;

import de.akquinet.jbosscc.guttenbase.connector.ConnectorInfo;
import de.akquinet.jbosscc.guttenbase.connector.Connector;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Connection info for importing data from a file.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ImportDumpConnectionInfo implements ConnectorInfo {
  private static final long serialVersionUID = 1L;

  private final String _path;

  public ImportDumpConnectionInfo(final String path) {
    assert path != null : "path != null";
    _path = path;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUser() {
    return "user";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPassword() {
    return "password";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSchema() {
    return "schema";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DatabaseType getDatabaseType() {
    return DatabaseType.IMPORT_DUMP;
  }

  public String getPath() {
    return _path;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Connector createConnector(final ConnectorRepository connectorRepository, final String connectorId) {
    return new ImportDumpConnector(connectorRepository, connectorId, this);
  }
}
