package de.akquinet.jbosscc.guttenbase.tools.mysql;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.AbstractTablesOperationTool;

/**
 * Will execute OPTIMIZE TABLE table;
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class MySqlReorgTablesTool extends AbstractTablesOperationTool
{
  public MySqlReorgTablesTool(final ConnectorRepository connectorRepository)
  {
    super(connectorRepository, "OPTIMIZE TABLE " + TABLE_PLACEHOLDER + ";");
  }

  public void executeOnAllTables(final String target) throws SQLException
  {
    executeOnAllTables(target, false, false);
  }
}
