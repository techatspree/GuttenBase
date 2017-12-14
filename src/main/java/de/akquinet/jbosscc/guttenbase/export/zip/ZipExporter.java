package de.akquinet.jbosscc.guttenbase.export.zip;

import de.akquinet.jbosscc.guttenbase.export.ExportDumpConnectorInfo;
import de.akquinet.jbosscc.guttenbase.export.ExportDumpExtraInformation;
import de.akquinet.jbosscc.guttenbase.export.ExportTableHeader;
import de.akquinet.jbosscc.guttenbase.export.Exporter;
import de.akquinet.jbosscc.guttenbase.hints.ExportDumpExtraInformationHint;
import de.akquinet.jbosscc.guttenbase.hints.ZipExporterClassResourcesHint;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.utils.Util;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Export schema information and data into executable JAR file. Since it is in ZIP file format the resulting file may as well be
 * inspected with a ZIP tool. The structure of the ZIP is based on the structure of a data base.
 * <p></p>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 * Hint is used by {@link ZipExporterClassResourcesHint} to add custom classes to the generated JAR and configure the
 * META-INF/MANIFEST.MF Main-Class entry
 * Hint is used by {@link ExportDumpExtraInformationHint} to add custom information to the generated JAR
 */
public class ZipExporter implements Exporter {
  private static final Logger LOG = Logger.getLogger(ZipExporter.class);

  private ZipOutputStream _zipOutputStream;
  private ObjectOutputStream _objectOutputStream;
  private File _tempFile;
  private ConnectorRepository _connectorRepository;
  private String _connectorId;
  private ExportDumpConnectorInfo _exportDumpConnectionInfo;

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeExport(final ConnectorRepository connectorRepository, final String connectorId,
                               final ExportDumpConnectorInfo exportDumpConnectionInfo) throws IOException {
    assert exportDumpConnectionInfo != null : "exportDumpConnectionInfo != null";
    assert connectorId != null : "connectorId != null";
    assert connectorRepository != null : "connectorRepository != null";

    _connectorRepository = connectorRepository;
    _connectorId = connectorId;
    _exportDumpConnectionInfo = exportDumpConnectionInfo;

    final File file = new File(exportDumpConnectionInfo.getPath());
    final FileOutputStream fos = new FileOutputStream(file);
    _zipOutputStream = new ZipOutputStream(fos);

    final ZipExporterClassResources zipExporterClassResources = connectorRepository.getConnectorHint(connectorId,
      ZipExporterClassResources.class).getValue();
    addClassesToJar(connectorRepository, connectorId, zipExporterClassResources);

    writeManifestEntry(zipExporterClassResources);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void finishExport() throws Exception {
    writeExtraInformation();

    final ZipExporterClassResources zipExporterClassResources = _connectorRepository.getConnectorHint(_connectorId,
      ZipExporterClassResources.class).getValue();
    addResourcesToJar(zipExporterClassResources);
    _zipOutputStream.close();
    _zipOutputStream = null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeDatabaseMetaData(final DatabaseMetaData databaseMetaData) throws IOException, SQLException {
    writeDatabaseEntry(databaseMetaData);

    for (final TableMetaData tableMetaData : databaseMetaData.getTableMetaData()) {
      writeTableEntry(tableMetaData);
      writeColumnEntries(tableMetaData);
      writeIndexEntries(tableMetaData);
    }
  }

  /**
   * {@inheritDoc}
   * <p></p>
   * Does nothing.
   */
  @Override
  public void writeTableHeader(final ExportTableHeader exportTableHeader) throws IOException, SQLException {
  }

  /**
   * {@inheritDoc}
   * <p></p>
   * Open new ZIP entry. Data will be written to a temporary file first, because otherwise it may exceed the memory.
   */
  @Override
  public void initializeWriteTableData(final TableMetaData tableMetaData) throws IOException {
    newEntry(ZipConstants.PREFIX + tableMetaData.getTableName() + ZipConstants.PATH_SEPARATOR + ZipConstants.TABLE_DATA_NAME);

    _tempFile = File.createTempFile("GB-JAR-", null);
    _tempFile.deleteOnExit();

    _objectOutputStream = new ObjectOutputStream(new FileOutputStream(_tempFile));
  }

  /**
   * {@inheritDoc}
   * <p></p>
   * Close current ZIP entry.
   */
  @Override
  public void finalizeWriteTableData(final TableMetaData table) throws IOException {
    _objectOutputStream.close();

    final FileInputStream fis = new FileInputStream(_tempFile);
    Util.copy(fis, _zipOutputStream);
    IOUtils.closeQuietly(fis);
    closeEntry();

    _tempFile.delete();
    _tempFile = null;
    _objectOutputStream = null;
  }

  /**
   * {@inheritDoc}
   * <p></p>
   * Does nothing.
   */
  @Override
  public void initializeWriteRowData(final TableMetaData table) {
  }

  /**
   * {@inheritDoc}
   * <p></p>
   * Does nothing.
   */
  @Override
  public void finalizeWriteRowData(final TableMetaData table) {
  }

  /**
   * Resets the output stream which reduces memory foot print drastically. See {@link ObjectOutputStream#reset()} for details.
   */
  @Override
  public void flush() throws IOException {
    if (_objectOutputStream != null) {
      _objectOutputStream.reset();
      _objectOutputStream.flush();
    }
  }

  @Override
  public void writeObject(final Object obj) throws IOException {
    _objectOutputStream.writeObject(obj);
  }

  private void writeIndexEntries(final TableMetaData tableMetaData) throws IOException {
    final String indexPath = ZipConstants.PREFIX + tableMetaData.getTableName() + ZipConstants.PATH_SEPARATOR
      + ZipConstants.INDEX_NAME + ZipConstants.PATH_SEPARATOR;

    for (final IndexMetaData indexMetaData : tableMetaData.getIndexes()) {
      newEntry(indexPath + indexMetaData.getIndexName() + ".txt");
      new ZipIndexMetaDataWriter().writeIndexMetaDataEntry(indexMetaData).store("Index meta data", _zipOutputStream);
      closeEntry();
    }
  }

  private void writeColumnEntries(final TableMetaData tableMetaData) throws IOException {
    final String columnPath = ZipConstants.PREFIX + tableMetaData.getTableName() + ZipConstants.PATH_SEPARATOR
      + ZipConstants.COLUMN_NAME + ZipConstants.PATH_SEPARATOR;

    for (final ColumnMetaData columnMetaData : tableMetaData.getColumnMetaData()) {
      newEntry(columnPath + columnMetaData.getColumnName() + ".txt");
      new ZipColumnMetaDataWriter().writeColumnMetaDataEntry(columnMetaData).store("Column meta data", _zipOutputStream);
      closeEntry();
    }
  }

  private void writeTableEntry(final TableMetaData tableMetaData) throws IOException {
    newEntry(ZipConstants.PREFIX + tableMetaData.getTableName() + ZipConstants.PATH_SEPARATOR + ZipConstants.TABLE_INFO_NAME);
    new ZipTableMetaDataWriter().writeTableMetaDataEntry(tableMetaData).store("Table meta data", _zipOutputStream);
    closeEntry();
  }

  private void writeDatabaseEntry(final DatabaseMetaData databaseMetaData) throws IOException, SQLException {
    newEntry(ZipConstants.PREFIX + ZipConstants.DBINFO_NAME);
    new ZipDatabaseMetaDataWriter().writeDatabaseMetaDataEntry(databaseMetaData).store("Database meta data", _zipOutputStream);
    closeEntry();

    newEntry(ZipConstants.META_DATA);
    _zipOutputStream.write(Util.toByteArray(databaseMetaData));
    closeEntry();
  }

  private void newEntry(final String name) throws IOException {
    _zipOutputStream.putNextEntry(new ZipEntry(name));
  }

  private void closeEntry() throws IOException {
    _zipOutputStream.closeEntry();
  }

  private void addClassesToJar(final ConnectorRepository connectorRepository, final String connectorId,
                               final ZipExporterClassResources zipExporterClassResources) throws IOException {
    final ZipClassesFromClassResourceExporter zipClassesFromClassResourceExporter = new ZipClassesFromClassResourceExporter(
      _zipOutputStream);

    for (final Class<?> clazz : zipExporterClassResources.getClassResources()) {
      zipClassesFromClassResourceExporter.copyClassesToZip(clazz);
    }
  }

  private void addResourcesToJar(final ZipExporterClassResources zipExporterClassResources) throws IOException {
    final ZipResourceExporter zipResourceExporter = new ZipResourceExporter(_zipOutputStream);

    for (final Entry<String, URL> entry : zipExporterClassResources.getUrlResources().entrySet()) {
      final URL url = entry.getValue();
      final String key = entry.getKey();

      if (url != null) {
        final InputStream stream = url.openStream();
        zipResourceExporter.addEntry(key, stream);
      } else {
        LOG.warn("Could not add null URL content for " + key);
      }
    }
  }

  private void writeManifestEntry(final ZipExporterClassResources zipExporterClassResources) throws IOException {
    newEntry(ZipConstants.MANIFEST_NAME);
    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    final PrintStream printStream = new PrintStream(bos);
    printStream.println("Manifest-Version: 1.0");
    printStream.println("Created-By: GuttenBase ZIP Exporter");
    printStream.println("Main-Class: " + zipExporterClassResources.getStartupClass().getName());
    printStream.close();
    _zipOutputStream.write(bos.toByteArray());
    closeEntry();
  }

  private void writeExtraInformation() throws SQLException, IOException {
    final ExportDumpExtraInformation exportDumpExtraInformation = _connectorRepository.getConnectorHint(_connectorId,
      ExportDumpExtraInformation.class).getValue();

    final Map<String, Serializable> extraInformation = exportDumpExtraInformation.getExtraInformation(_connectorRepository,
      _connectorId, _exportDumpConnectionInfo);

    for (final Entry<String, Serializable> entry : extraInformation.entrySet()) {
      newEntry(ZipConstants.EXTRA_INFO + ZipConstants.PATH_SEPARATOR + entry.getKey());
      final byte[] byteArray = Util.toByteArray(entry.getValue());
      _zipOutputStream.write(byteArray);
      closeEntry();
    }
  }
}
