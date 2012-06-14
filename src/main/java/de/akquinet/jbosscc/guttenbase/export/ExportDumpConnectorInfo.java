package de.akquinet.jbosscc.guttenbase.export;

import de.akquinet.jbosscc.guttenbase.connector.ConnectorInfo;
import de.akquinet.jbosscc.guttenbase.connector.Connector;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Connection info for exporting data to a file.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ExportDumpConnectorInfo implements ConnectorInfo {
  private static final long serialVersionUID = 1L;

  private final String _path;
  private final String _sourceConnectorId;

  public ExportDumpConnectorInfo(final String sourceConnectorId, final String path) {
    assert sourceConnectorId != null : "sourceConnectorId != null";
    assert path != null : "path != null";
    _sourceConnectorId = sourceConnectorId;
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
    return DatabaseType.EXPORT_DUMP;
  }

  public String getPath() {
    return _path;
  }

  public String getSourceConnectorId() {
    return _sourceConnectorId;
  }

  @Override
  public Connector createConnector(final ConnectorRepository connectorRepository, final String connectorId) {
    return new ExportDumpConnector(connectorRepository, connectorId, this);
  }
}
