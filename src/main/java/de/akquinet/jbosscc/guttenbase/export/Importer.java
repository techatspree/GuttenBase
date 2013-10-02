package de.akquinet.jbosscc.guttenbase.export;

import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Import schema information and table data from some custom format.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface Importer {
	void initializeImport(final ConnectorRepository connectorRepository, final String connectorId,
			final ImportDumpConnectionInfo importDumpConnectionInfo) throws Exception;

	void finishImport() throws Exception;

	DatabaseMetaData readDatabaseMetaData() throws Exception;

	Object readObject() throws Exception;

	void seekTableHeader(TableMetaData tableMetaData) throws Exception;
}