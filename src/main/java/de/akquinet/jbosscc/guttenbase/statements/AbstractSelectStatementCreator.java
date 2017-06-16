package de.akquinet.jbosscc.guttenbase.statements;

import de.akquinet.jbosscc.guttenbase.hints.ColumnOrderHint;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;
import de.akquinet.jbosscc.guttenbase.tools.ResultSetParameters;
import de.akquinet.jbosscc.guttenbase.tools.SelectWhereClause;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Create SELECT statement for copying data.
 * <p/>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public abstract class AbstractSelectStatementCreator extends AbstractStatementCreator {
  public static final String FROM = " FROM ";

  public AbstractSelectStatementCreator(final ConnectorRepository connectorRepository, final String connectorId) {
    super(connectorRepository, connectorId);
  }

  /**
   * Create SELECT statement in the source table to retrieve data from the configured source columns.
   */
  public PreparedStatement createSelectStatement(final Connection connection, final String tableName, final TableMetaData tableMetaData) throws SQLException {
    final ResultSetParameters resultSetParameters = _connectorRepository.getConnectorHint(_connectorId, ResultSetParameters.class).getValue();
    final List<ColumnMetaData> columns = ColumnOrderHint.getSortedColumns(_connectorRepository, _connectorId, tableMetaData);
    final String sql = createSQL(tableName, tableMetaData, columns);

    LOG.debug("Create SELECT statement " + sql);

    final PreparedStatement preparedStatement = connection.prepareStatement(sql, resultSetParameters.getResultSetType(tableMetaData), resultSetParameters.getResultSetConcurrency(tableMetaData));

    preparedStatement.setFetchSize(Math.min(resultSetParameters.getFetchSize(tableMetaData), preparedStatement.getMaxRows()));

    return preparedStatement;
  }

  protected String createWhereClause(final TableMetaData tableMetaData) throws SQLException {
    final SelectWhereClause selectWhereClause = _connectorRepository.getConnectorHint(_connectorId, SelectWhereClause.class).getValue();
    return selectWhereClause.getWhereClause(tableMetaData);
  }

  /**
   * Create SELECT statement in the target table to retrieve data from the mapped columns. I.e., since the target table
   * configuration may be different, the SELECT statement may be different. This is needed to check data compatibility with the
   * {@link CheckEqualTableDataTool}
   */
  public PreparedStatement createMappedSelectStatement(final Connection connection, final TableMetaData sourceTableMetaData, final String tableName,
                                                       final TableMetaData targetTableMetaData, final String sourceConnectorId) throws SQLException {
    final List<ColumnMetaData> columns = getMappedTargetColumns(sourceTableMetaData, targetTableMetaData, sourceConnectorId);
    final String sql = createSQL(tableName, targetTableMetaData, columns);

    return connection.prepareStatement(sql);
  }

  /**
   * Try to retrieve data in some deterministic order
   */
  protected String createOrderBy(final TableMetaData tableMetaData) throws SQLException {
    return "";
  }

  private String createSQL(final String tableName, final TableMetaData tableMetaData, final List<ColumnMetaData> columns)
    throws SQLException {

    return "SELECT " + createColumnClause(columns) +
      FROM + tableName +
      " " + createWhereClause(tableMetaData) +
      " " + createOrderBy(tableMetaData);
  }
}
