package de.akquinet.jbosscc.guttenbase.tools.mysql;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.AbstractTablesOperationTool;

import java.sql.SQLException;

/**
 * Will execute OPTIMIZE TABLE table;
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class MySqlReorgTablesTool extends AbstractTablesOperationTool {
  public MySqlReorgTablesTool(final ConnectorRepository connectorRepository) {
    super(connectorRepository, "OPTIMIZE TABLE " + TABLE_PLACEHOLDER + ";");
  }

  public void executeOnAllTables(final String target) throws SQLException {
    executeOnAllTables(target, false, false);
  }
}
