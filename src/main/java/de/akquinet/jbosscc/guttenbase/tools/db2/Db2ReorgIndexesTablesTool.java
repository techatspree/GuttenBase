package de.akquinet.jbosscc.guttenbase.tools.db2;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.AbstractTablesOperationTool;

/**
 * Will execute REORG INDEXES ALL FOR TABLE table;
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class Db2ReorgIndexesTablesTool extends AbstractTablesOperationTool
{
  public Db2ReorgIndexesTablesTool(final ConnectorRepository connectorRepository)
  {
    super(connectorRepository, "CALL SYSPROC.ADMIN_CMD('REORG INDEXES ALL FOR TABLE " + TABLE_PLACEHOLDER + "');");
  }

  public void executeOnAllTables(final String target) throws SQLException
  {
    executeOnAllTables(target, false, false);
  }

  @Override
  public boolean isApplicableOnTable(final TableMetaData tableMetaData)
  {
    // Prevent DB2 SQL Error: SQLCODE=-1146, SQLSTATE=01H52
    return !tableMetaData.getIndexes().isEmpty();
  }
}
