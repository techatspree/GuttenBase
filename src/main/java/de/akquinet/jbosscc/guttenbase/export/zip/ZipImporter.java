package de.akquinet.jbosscc.guttenbase.export.zip;

import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.akquinet.jbosscc.guttenbase.export.ImportDumpConnectionInfo;
import de.akquinet.jbosscc.guttenbase.export.ImportDumpExtraInformation;
import de.akquinet.jbosscc.guttenbase.export.Importer;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.utils.Util;

/**
 * Read database information and data from ZIP file.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ZipImporter implements Importer {
	private ZipFile _zipFile;
	private ObjectInputStream _objectInputStream;
	private ConnectorRepository _connectorRepository;
	private String _connectorId;

	@Override
	public void initializeImport(final ConnectorRepository connectorRepository, final String connectorId,
			final ImportDumpConnectionInfo importDumpConnectionInfo) throws Exception {
		assert importDumpConnectionInfo != null : "importDumpConnectionInfo != null";
		assert connectorId != null : "connectorId != null";
		assert connectorRepository != null : "connectorRepository != null";

		final File file = new File(importDumpConnectionInfo.getPath());

		_connectorRepository = connectorRepository;
		_connectorId = connectorId;
		_zipFile = new ZipFile(file);
	}

	@Override
	public void finishImport() throws Exception {
		readExtraInformation();

		_zipFile.close();
		_zipFile = null;
	}

	@Override
	public DatabaseMetaData readDatabaseMetaData() throws Exception {
		final ZipEntry zipEntry = _zipFile.getEntry(ZipConstants.META_DATA);

		assert zipEntry != null : "zipEntry != null";

		final InputStream inputStream = _zipFile.getInputStream(zipEntry);

		final ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		final DatabaseMetaData databaseMetaData = (DatabaseMetaData) objectInputStream.readObject();
		objectInputStream.close();

		return databaseMetaData;
	}

	@Override
	public Object readObject() throws Exception {
		return _objectInputStream.readObject();
	}

	@Override
	public void seekTableHeader(final TableMetaData tableMetaData) throws Exception {
		if (_objectInputStream != null) {
			_objectInputStream.close();
		}

		final ZipEntry zipEntry = _zipFile.getEntry(ZipConstants.PREFIX + tableMetaData.getTableName() + ZipConstants.PATH_SEPARATOR
				+ ZipConstants.TABLE_DATA_NAME);

		assert zipEntry != null : "zipEntry != null";

		_objectInputStream = new ObjectInputStream(_zipFile.getInputStream(zipEntry));
	}

	private void readExtraInformation() throws Exception {
		final ImportDumpExtraInformation importDumpExtraInformation = _connectorRepository.getConnectorHint(_connectorId,
				ImportDumpExtraInformation.class).getValue();
		final Map<String, Serializable> extraInformation = new HashMap<String, Serializable>();

		final String prefix = ZipConstants.EXTRA_INFO + ZipConstants.PATH_SEPARATOR;

		for (final Enumeration<? extends ZipEntry> entries = _zipFile.entries(); entries.hasMoreElements();) {
			final ZipEntry zipEntry = entries.nextElement();
			final String name = zipEntry.getName();

			if (name.startsWith(prefix)) {
				final InputStream inputStream = _zipFile.getInputStream(zipEntry);
				final String key = name.substring(prefix.length());
				final Serializable value = Util.fromInputStream(Serializable.class, inputStream);
				inputStream.close();

				extraInformation.put(key, value);
			}
		}

		importDumpExtraInformation.processExtraInformation(extraInformation);
	}
}
