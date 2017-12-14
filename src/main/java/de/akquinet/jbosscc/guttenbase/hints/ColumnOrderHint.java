package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnOrderComparatorFactory;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.statements.AbstractStatementCreator;
import de.akquinet.jbosscc.guttenbase.statements.InsertStatementFiller;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Determine order of columns in SELECT statement. This will of course also influence the ordering of the resulting INSERT statement.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 *
 * Hint is used by {@link AbstractStatementCreator} to determine column order
 * Hint is used by {@link InsertStatementFiller} to determine column order
 * Hint is used by {@link CheckEqualTableDataTool} to determine column order
 *
 * @author M. Dahm
 */
public abstract class ColumnOrderHint implements ConnectorHint<ColumnOrderComparatorFactory> {
	@Override
	public Class<ColumnOrderComparatorFactory> getConnectorHintType() {
		return ColumnOrderComparatorFactory.class;
	}

	/**
	 * Helper method
	 */
	public static List<ColumnMetaData> getSortedColumns(final ConnectorRepository connectorRepository, final String connectorId,
			final TableMetaData tableMetaData) {
		final Comparator<ColumnMetaData> sourceColumnComparator = connectorRepository
				.getConnectorHint(connectorId, ColumnOrderComparatorFactory.class).getValue().createComparator();
		final List<ColumnMetaData> columns = new ArrayList<>(tableMetaData.getColumnMetaData());
		columns.sort(sourceColumnComparator);

		return columns;
	}
}
