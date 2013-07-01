package de.akquinet.jbosscc.guttenbase.statements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.hints.ColumnOrderHint;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;

/**
 * Create SELECT statement for copying data.
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public abstract class AbstractSelectStatementCreator extends AbstractStatementCreator
{
  public static final String FROM = " FROM ";

  public AbstractSelectStatementCreator(final ConnectorRepository connectorRepository, final String connectorId)
  {
    super(connectorRepository, connectorId);
  }

  /**
   * Create SELECT statement in the source table to retrieve data from the configured source columns.
   */
  public PreparedStatement createSelectStatement(final String tableName, final TableMetaData tableMetaData,
      final Connection connection) throws SQLException
  {
    final List<ColumnMetaData> columns = ColumnOrderHint.getSortedColumns(_connectorRepository, _connectorId, tableMetaData);
    final String sql = createSQL(tableName, tableMetaData, columns);
    LOG.debug("Create SELECT statement " + sql);

    return connection.prepareStatement(sql);
  }

  /**
   * Create SELECT statement in the target table to retrieve data from the mapped columns. I.e., since the target table
   * configuration may be different, the SELECT statement may be different. This is needed to check data compatibility with the
   * {@link CheckEqualTableDataTool}
   */
  public PreparedStatement createMappedSelectStatement(final TableMetaData sourceTableMetaData, final String tableName,
      final TableMetaData targetTableMetaData, final Connection connection, final String sourceConnectorId) throws SQLException
  {
    final List<ColumnMetaData> columns = getMappedTargetColumns(sourceTableMetaData, targetTableMetaData, sourceConnectorId);
    final String sql = createSQL(tableName, targetTableMetaData, columns);

    return connection.prepareStatement(sql);
  }

  /**
   * Try to retrieve data in some deterministic order
   */
  protected String createOrderBy(final TableMetaData tableMetaData) throws SQLException
  {
    return "";
  }

  private String createSQL(final String tableName, final TableMetaData tableMetaData, final List<ColumnMetaData> columns)
      throws SQLException
  {
    final StringBuilder buf = new StringBuilder("SELECT ");

    buf.append(createColumnClause(columns));
    buf.append(FROM + tableName);

    buf.append(" " + createWhereClause(tableMetaData));
    buf.append(" " + createOrderBy(tableMetaData));

    return buf.toString();
  }
}
