package de.akquinet.jbosscc.guttenbase.export.plain;

import de.akquinet.jbosscc.guttenbase.export.ExportTableHeader;
import de.akquinet.jbosscc.guttenbase.export.ImportDumpConnectionInfo;
import de.akquinet.jbosscc.guttenbase.export.ImportDumpExtraInformation;
import de.akquinet.jbosscc.guttenbase.export.Importer;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Import schema information and data from gzipped {@link ObjectInputStream} file with serialized data.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class PlainGzipImporter implements Importer {
  private ObjectInputStream _objectInputStream;
  private ConnectorRepository _connectorRepository;
  private String _connectorId;
  // Ensure that table data has been read when seeking the extra informations
  private boolean _tableDataRead = false;

  @Override
  public void initializeImport(final ConnectorRepository connectorRepository, final String connectorId,
      final ImportDumpConnectionInfo importDumpConnectionInfo) throws IOException {
    assert importDumpConnectionInfo != null : "importDumpConnectionInfo != null";
    assert connectorId != null : "connectorId != null";
    assert connectorRepository != null : "connectorRepository != null";

    _connectorRepository = connectorRepository;
    _connectorId = connectorId;
    final GZIPInputStream gzipInputStream = new GZIPInputStream(importDumpConnectionInfo.getPath().openStream());
    _objectInputStream = new ObjectInputStream(gzipInputStream);
  }

  @Override
  public void finishImport() throws Exception {
    if (_tableDataRead) { // At end
      readExtraInformation();
    }

    _objectInputStream.close();
    _objectInputStream = null;
  }

  @Override
  public DatabaseMetaData readDatabaseMetaData() throws Exception {
    return (DatabaseMetaData) _objectInputStream.readObject();
  }

  @Override
  public void seekTableHeader(final TableMetaData tableMetaData) throws Exception {
    _tableDataRead = true;
    ExportTableHeader exportTableHeader;

    do {
      exportTableHeader = seekNextTableHeader();
    } while (!tableMetaData.getTableName().equalsIgnoreCase(exportTableHeader.getTableName()));
  }

  @Override
  public Object readObject() throws Exception {
    return _objectInputStream.readObject();
  }

  private ExportTableHeader seekNextTableHeader() throws Exception {
    Object value;

    do {
      value = _objectInputStream.readObject();
    } while (!(value instanceof ExportTableHeader));

    return (ExportTableHeader) value;
  }

  private void readExtraInformation() throws Exception {
    final ImportDumpExtraInformation importDumpExtraInformation = _connectorRepository.getConnectorHint(_connectorId,
        ImportDumpExtraInformation.class).getValue();

    @SuppressWarnings("unchecked")
    final Map<String, Serializable> extraInformation = (Map<String, Serializable>) readObject();

    importDumpExtraInformation.processExtraInformation(extraInformation);
  }
}
