package de.akquinet.jbosscc.guttenbase.export;

import de.akquinet.jbosscc.guttenbase.connector.impl.AbstractConnector;
import de.akquinet.jbosscc.guttenbase.exceptions.ImportException;
import de.akquinet.jbosscc.guttenbase.hints.ImporterFactoryHint;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connection info for importing data from a file.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * Hint is used by {@link ImporterFactoryHint} to determine importer implementation
 * @author M. Dahm
 */
public class ImportDumpConnector extends AbstractConnector
{
  private final ImportDumpConnectionInfo _importDumpConnectionInfo;
  private DatabaseMetaData _databaseMetaData;

  public ImportDumpConnector(
      final ConnectorRepository connectorRepository,
      final String connectorId,
      final ImportDumpConnectionInfo importDumpConnectionInfo)
  {
    super(connectorRepository, connectorId, importDumpConnectionInfo);

    _importDumpConnectionInfo = importDumpConnectionInfo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Connection openConnection() throws SQLException
  {
    if (_connection == null || _connection.isClosed())
    {
      try
      {
        final Importer importer = _connectorRepository.getConnectorHint(_connectorId, ImporterFactory.class).getValue()
            .createImporter();

        importer.initializeImport(_connectorRepository, _connectorId, _importDumpConnectionInfo);

        _databaseMetaData = importer.readDatabaseMetaData();
        _connection = new ImportDumpConnection(importer, _databaseMetaData);
      }
      catch (final Exception e)
      {
        throw new ImportException("openConnection", e);
      }
    }

    return _connection;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DatabaseMetaData retrieveDatabaseMetaData() throws SQLException
  {
    // Make sure the information is there
    openConnection();
    closeConnection();

    return _databaseMetaData;
  }
}
