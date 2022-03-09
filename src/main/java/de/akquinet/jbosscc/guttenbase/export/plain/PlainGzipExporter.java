package de.akquinet.jbosscc.guttenbase.export.plain;

import de.akquinet.jbosscc.guttenbase.export.ExportDumpConnectorInfo;
import de.akquinet.jbosscc.guttenbase.export.ExportDumpExtraInformation;
import de.akquinet.jbosscc.guttenbase.export.ExportTableHeader;
import de.akquinet.jbosscc.guttenbase.export.Exporter;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

import java.io.*;
import java.sql.SQLException;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 * Export schema information and data into gzipped {@link ObjectOutputStream} file with serialized data.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class PlainGzipExporter implements Exporter {
  private ObjectOutputStream _objectOutputStream;
  private ConnectorRepository _connectorRepository;
  private String _connectorId;
  private ExportDumpConnectorInfo _exportDumpConnectionInfo;

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeExport(final ConnectorRepository connectorRepository, final String connectorId,
                               final ExportDumpConnectorInfo exportDumpConnectionInfo) throws Exception {
    assert exportDumpConnectionInfo != null : "exportDumpConnectionInfo != null";
    assert connectorId != null : "connectorId != null";
    assert connectorRepository != null : "connectorRepository != null";

    _connectorRepository = connectorRepository;
    _connectorId = connectorId;
    _exportDumpConnectionInfo = exportDumpConnectionInfo;

    openOutputStream(exportDumpConnectionInfo);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void finishExport() throws Exception {
    writeExtraInformation();
    _objectOutputStream.close();
    _objectOutputStream = null;
  }

  @Override
  public void writeTableHeader(final ExportTableHeader exportTableHeader) throws IOException {
    _objectOutputStream.writeObject(exportTableHeader);
  }

  @Override
  public void writeDatabaseMetaData(final DatabaseMetaData databaseMetaData) throws IOException {
    _objectOutputStream.writeObject(databaseMetaData);
  }

  @Override
  public void initializeWriteTableData(final TableMetaData table) {
  }

  @Override
  public void finalizeWriteTableData(final TableMetaData table) {
  }

  @Override
  public void initializeWriteRowData(final TableMetaData table) {
  }

  @Override
  public void finalizeWriteRowData(final TableMetaData table) {
  }

  /**
   * Resets the output stream which reduces memory foot print drastically. See {@link ObjectOutputStream#reset()} for details.
   */
  @Override
  public void flush() throws IOException {
    _objectOutputStream.reset();
    _objectOutputStream.flush();
  }

  @Override
  public void writeObject(final Object obj) throws IOException {
    _objectOutputStream.writeObject(obj);
  }

  private void writeExtraInformation() throws IOException, SQLException {
    final ExportDumpExtraInformation exportDumpExtraInformation = _connectorRepository.getConnectorHint(_connectorId,
        ExportDumpExtraInformation.class).getValue();

    final Map<String, Serializable> extraInformation = exportDumpExtraInformation.getExtraInformation(_connectorRepository, _connectorId,
        _exportDumpConnectionInfo);

    writeObject(extraInformation);
  }

  private void openOutputStream(final ExportDumpConnectorInfo exportDumpConnectionInfo) throws IOException {
    final File file = new File(exportDumpConnectionInfo.getPath());
    final FileOutputStream fileOutputStream = new FileOutputStream(file);
    final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
    _objectOutputStream = new ObjectOutputStream(gzipOutputStream);
  }
}
