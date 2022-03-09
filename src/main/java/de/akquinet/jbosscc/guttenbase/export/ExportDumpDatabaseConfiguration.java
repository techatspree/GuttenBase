package de.akquinet.jbosscc.guttenbase.export;

import de.akquinet.jbosscc.guttenbase.configuration.impl.DefaultTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Export dump configuration forwards important events to {@link ExportDumpConnection}.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ExportDumpDatabaseConfiguration extends DefaultTargetDatabaseConfiguration {
	public ExportDumpDatabaseConfiguration(final ConnectorRepository connectorRepository) {
		super(connectorRepository);
	}

	@Override
	public void afterInsert(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException {
		System.gc();
	}

	@Override
	public void beforeTableCopy(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException {
		((ExportDumpConnection) connection).initializeWriteTableData(table);
	}

	@Override
	public void afterTableCopy(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException {
		((ExportDumpConnection) connection).finalizeWriteTableData(table);
	}

	@Override
	public void beforeNewRow(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException {
		((ExportDumpConnection) connection).initializeWriteRowData(table);
	}

	@Override
	public void afterNewRow(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException {
		((ExportDumpConnection) connection).finalizeWriteRowData(table);
	}
}
