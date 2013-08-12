package de.akquinet.jbosscc.guttenbase.tools.db2;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.AbstractTablesOperationTool;

/**
 * Will execute REORG INDEXES ALL FOR TABLE table;
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class Db2ReorgIndexesTablesTool extends AbstractTablesOperationTool
{
  public Db2ReorgIndexesTablesTool(final ConnectorRepository connectorRepository)
  {
    super(connectorRepository, "REORG INDEXES ALL FOR TABLE " + TABLE_PLACEHOLDER + ";");
  }

  public void executeOnAllTables(final String target) throws SQLException
  {
    executeOnAllTables(target, false, false);
  }
}
