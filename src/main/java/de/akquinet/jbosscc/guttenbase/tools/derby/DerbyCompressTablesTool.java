package de.akquinet.jbosscc.guttenbase.tools.derby;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.AbstractTablesOperationTool;

import java.sql.SQLException;

/**
 * Will execute SYSCS_UTIL.SYSCS_INPLACE_COMPRESS_TABLE system procedure
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DerbyCompressTablesTool extends AbstractTablesOperationTool
{
  public DerbyCompressTablesTool(final ConnectorRepository connectorRepository)
  {
    super(connectorRepository, "CALL SYSCS_UTIL.SYSCS_INPLACE_COMPRESS_TABLE ('sa', '" + TABLE_PLACEHOLDER + "', 1, 1, 1);");
  }

  public void executeOnAllTables(final String target) throws SQLException
  {
    executeOnAllTables(target, false, false);
  }
}
