package de.akquinet.jbosscc.guttenbase.tools.mssql;

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
public class MSSqlReorgTablesTool extends AbstractTablesOperationTool {
  public MSSqlReorgTablesTool(final ConnectorRepository connectorRepository) {
    super(connectorRepository, "ALTER INDEX ALL ON " + TABLE_PLACEHOLDER +
        " REBUILD WITH (FILLFACTOR = 80, SORT_IN_TEMPDB = ON, STATISTICS_NORECOMPUTE = ON");
  }

  public void executeOnAllTables(final String target) throws SQLException {
    executeOnAllTables(target, false, false);
  }
}
