package de.akquinet.jbosscc.guttenbase.export;

import java.sql.Connection;
import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.configuration.impl.DefaultSourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Import dump configuration forwards important events to {@link ImportDumpConnection}.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ImportDumpDatabaseConfiguration extends DefaultSourceDatabaseConfiguration {
	private static final long serialVersionUID = 1L;

	public ImportDumpDatabaseConfiguration(final ConnectorRepository connectorRepository) {
		super(connectorRepository);
	}

	@Override
	public void beforeTableCopy(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException {
		((ImportDumpConnection) connection).initializeReadTable(table);
	}

	@Override
	public void afterTableCopy(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException {
		((ImportDumpConnection) connection).finalizeReadTable(table);
	}
}
