package de.akquinet.jbosscc.guttenbase.hints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnOrderComparatorFactory;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.statements.AbstractStatementCreator;
import de.akquinet.jbosscc.guttenbase.statements.InsertStatementFiller;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;

/**
 * Determine order of columns in SELECT statement. This will of course also influence the ordering of the resulting INSERT statement.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @gb.ApplicableForSource
 * @gb.HintUsedBy {@link AbstractStatementCreator} to determine column order
 * @gb.HintUsedBy {@link InsertStatementFiller} to determine column order
 * @gb.HintUsedBy {@link CheckEqualTableDataTool} to determine column order
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
		final List<ColumnMetaData> columns = new ArrayList<ColumnMetaData>(tableMetaData.getColumnMetaData());
		Collections.sort(columns, sourceColumnComparator);

		return columns;
	}
}
