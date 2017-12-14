package de.akquinet.jbosscc.guttenbase.statements;

import de.akquinet.jbosscc.guttenbase.hints.SplitColumnHint;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.SplitColumn;
import java.sql.SQLException;

/**
 * Sometimes the amount of data exceeds any buffer. In these cases we need to split the data by some given range, usually the primary key.
 * I.e., the data is read in chunks where these chunks are split using the ID column range of values.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * Hint is used by {@link SplitColumnHint}
 * @author M. Dahm
 */
public class SplitByColumnSelectStatementCreator extends AbstractSelectStatementCreator {
  public SplitByColumnSelectStatementCreator(final ConnectorRepository connectorRepository, final String connectorId) {
    super(connectorRepository, connectorId);
  }

  @Override
  protected String createWhereClause(final TableMetaData tableMetaData) throws SQLException {
    final ColumnMetaData splitColumn = _connectorRepository.getConnectorHint(_connectorId, SplitColumn.class).getValue()
        .getSplitColumn(tableMetaData);
    return "WHERE " + splitColumn.getColumnName() + " BETWEEN ? AND ?";
  }

  @Override
  protected String createOrderBy(final TableMetaData tableMetaData) throws SQLException {
    final ColumnMetaData splitColumn = _connectorRepository.getConnectorHint(_connectorId, SplitColumn.class).getValue()
        .getSplitColumn(tableMetaData);
    return "ORDER BY " + splitColumn.getColumnName();
  }
}
