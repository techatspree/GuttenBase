package de.akquinet.jbosscc.guttenbase.statements;

import de.akquinet.jbosscc.guttenbase.hints.SplitColumnHint;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.SplitColumn;

import java.util.List;

/**
 * Sometimes the amount of data in the result set exceeds any buffer. In these cases we need to split the data by some given range, usually
 * the primary key. I.e., the data is read in chunks where these chunks are split using the ID column range of values.
 * <p>
 * With this statement we count the number of rows that actually will be read for the given chunk.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 * <p>
 * Hint is used by {@link SplitColumnHint}
 *
 * @author M. Dahm
 */
public class SplitByColumnSelectCountStatementCreator extends AbstractSelectStatementCreator {
  public SplitByColumnSelectCountStatementCreator(final ConnectorRepository connectorRepository, final String connectorId) {
    super(connectorRepository, connectorId);
  }

  @Override
  protected String createColumnClause(final List<ColumnMetaData> columns) {
    return "COUNT(*)";
  }

  @Override
  protected String createWhereClause(final TableMetaData tableMetaData) {
    final ColumnMetaData splitColumn = _connectorRepository.getConnectorHint(_connectorId, SplitColumn.class).getValue()
        .getSplitColumn(tableMetaData);

    return "WHERE " + splitColumn.getColumnName() + " BETWEEN ? AND ?";
  }
}
