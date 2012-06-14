package de.akquinet.jbosscc.guttenbase.tools.postgresql;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.AbstractTablesOperationTool;

/**
 * Will execute VACUUM ANALYZE table;
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class PostgresqlVacuumTablesTool extends AbstractTablesOperationTool {
	public PostgresqlVacuumTablesTool(final ConnectorRepository connectorRepository) {
		super(connectorRepository, "VACUUM ANALYZE " + TABLE_PLACEHOLDER + ";");
	}

	public void executeOnAllTables(final String source) throws SQLException {
		executeOnAllTables(source, false, false);
	}
}
