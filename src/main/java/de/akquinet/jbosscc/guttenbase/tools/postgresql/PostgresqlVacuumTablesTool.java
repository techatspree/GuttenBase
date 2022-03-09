package de.akquinet.jbosscc.guttenbase.tools.postgresql;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.AbstractTablesOperationTool;

import java.sql.SQLException;

/**
 * Will execute VACUUM ANALYZE table;
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class PostgresqlVacuumTablesTool extends AbstractTablesOperationTool {
  public PostgresqlVacuumTablesTool(final ConnectorRepository connectorRepository) {
    super(connectorRepository, "VACUUM ANALYZE " + TABLE_PLACEHOLDER + ";");
  }

  public void executeOnAllTables(final String connectorId) throws SQLException {
    executeOnAllTables(connectorId, false, false);
  }
}
