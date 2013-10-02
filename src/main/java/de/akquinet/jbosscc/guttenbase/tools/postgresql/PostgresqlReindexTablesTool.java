package de.akquinet.jbosscc.guttenbase.tools.postgresql;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.AbstractTablesOperationTool;

/**
 * Will execute REINDEX TABLE table;
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class PostgresqlReindexTablesTool extends AbstractTablesOperationTool {
	public PostgresqlReindexTablesTool(final ConnectorRepository connectorRepository) {
		super(connectorRepository, "REINDEX TABLE " + TABLE_PLACEHOLDER + ";");
	}

	public void executeOnAllTables(final String target) throws SQLException {
		executeOnAllTables(target, false, false);
	}
}
