package de.akquinet.jbosscc.guttenbase.tools.db2;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.AbstractTablesOperationTool;

/**
 * Will execute REORG TABLE table;
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class Db2ReorgTablesTool extends AbstractTablesOperationTool
{
  public Db2ReorgTablesTool(final ConnectorRepository connectorRepository)
  {
    super(connectorRepository, "CALL SYSPROC.ADMIN_CMD('REORG TABLE " + TABLE_PLACEHOLDER + "');");
  }

  public void executeOnAllTables(final String target) throws SQLException
  {
    executeOnAllTables(target, false, false);
  }
}
