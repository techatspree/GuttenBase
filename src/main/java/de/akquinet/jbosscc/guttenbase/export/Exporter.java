package de.akquinet.jbosscc.guttenbase.export;

import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Export schema information and table data to some custom format.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public interface Exporter {
	/**
	 * Start exporting to a file.
	 */
	void initializeExport(ConnectorRepository connectorRepository, String connectorId, final ExportDumpConnectorInfo exportDumpConnectionInfo)
			throws Exception;

	/**
	 * Finish export
	 */
	void finishExport() throws Exception;

	/**
	 * Write table header when executing an INSERT statement. This is necessary to mark where data for a given table starts since some tables
	 * may be skipped during import. The header is written only once in fact.
	 */
	void writeTableHeader(ExportTableHeader exportTableHeader) throws Exception;

	/**
	 * Dump database information
	 */
	void writeDatabaseMetaData(DatabaseMetaData sourceDatabaseMetaData) throws Exception;

	/**
	 * Called before copying of a table starts.
	 */
	void initializeWriteTableData(TableMetaData tableMetaData) throws Exception;

	/**
	 * Called after copying of a table ends.
	 */
	void finalizeWriteTableData(TableMetaData tableMetaData) throws Exception;

	/**
	 * Called before copying of a table row starts.
	 */
	void initializeWriteRowData(TableMetaData tableMetaData) throws Exception;

	/**
	 * Called after copying of a table row ends.
	 */
	void finalizeWriteRowData(TableMetaData tableMetaData) throws Exception;

	/**
	 * Allow the implementation to flush its buffers. This method is called by {@link ExportDumpConnection#commit()}.
	 */
	void flush() throws Exception;

	void writeObject(Object obj) throws Exception;
}
