package de.akquinet.jbosscc.guttenbase.export;

import de.akquinet.jbosscc.guttenbase.connector.impl.AbstractConnector;
import de.akquinet.jbosscc.guttenbase.exceptions.ExportException;
import de.akquinet.jbosscc.guttenbase.hints.ExporterFactoryHint;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalTableMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.utils.Util;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connection info for exporting data base contents to a file.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @gb.UsesHint {@link ExporterFactoryHint} to determine exporter implementation
 * @author M. Dahm
 */
public class ExportDumpConnector extends AbstractConnector {
  private final ExportDumpConnectorInfo _exportDumpConnectionInfo;

  public ExportDumpConnector(final ConnectorRepository connectorRepository, final String connectorId,
      final ExportDumpConnectorInfo exportDumpConnectionInfo) {
    super(connectorRepository, connectorId, exportDumpConnectionInfo);

    _exportDumpConnectionInfo = exportDumpConnectionInfo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Connection openConnection() throws SQLException {
    if (_connection == null || _connection.isClosed()) {
      try {
        final Exporter exporter = _connectorRepository.getConnectorHint(_connectorId, ExporterFactory.class).getValue().createExporter();

        exporter.initializeExport(_connectorRepository, _connectorId, _exportDumpConnectionInfo);
        exporter.writeDatabaseMetaData(retrieveSourceDatabaseMetaData());
        _connection = new ExportDumpConnection(exporter);
      } catch (final Exception e) {
        throw new ExportException("openConnection", e);
      }
    }

    return _connection;
  }

  /**
   * Table meta data is the same as the meta data of the source connector. The only difference is that the row count of all tables is reset
   * to 0.
   *
   * {@inheritDoc}
   */
  @Override
  public DatabaseMetaData retrieveDatabaseMetaData() throws SQLException {
    final DatabaseMetaData data = retrieveSourceDatabaseMetaData();
    final DatabaseMetaData result = Util.copyObject(DatabaseMetaData.class, data);

    for (final TableMetaData tableMetaData : result.getTableMetaData()) {
      ((InternalTableMetaData) tableMetaData).setTotalRowCount(0);
    }

    return result;
  }

  private DatabaseMetaData retrieveSourceDatabaseMetaData() throws SQLException {
    return _connectorRepository.getDatabaseMetaData(_exportDumpConnectionInfo.getSourceConnectorId());
  }
}
