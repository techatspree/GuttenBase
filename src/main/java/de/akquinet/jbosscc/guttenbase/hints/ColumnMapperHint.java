package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.statements.AbstractStatementCreator;
import de.akquinet.jbosscc.guttenbase.tools.AbstractTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;

/**
 * Select target column(s) for given source column. Usually, there will a 1:1 relationship. However, there may be situations where you want
 * to duplicate or transform data into multiple columns.
 *
 * Alternatively the list may also be empty.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 *
 * Hint is used by {@link CheckEqualTableDataTool} to map columns
 * Hint is used by {@link de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool} to map columns
 * Hint is used by {@link AbstractTableCopyTool} to map columns
 * Hint is used by {@link AbstractStatementCreator} to map columns
 *
 * @author M. Dahm
 */
@SuppressWarnings("deprecation")
public abstract class ColumnMapperHint implements ConnectorHint<ColumnMapper> {
	@Override
	public Class<ColumnMapper> getConnectorHintType() {
		return ColumnMapper.class;
	}
}
