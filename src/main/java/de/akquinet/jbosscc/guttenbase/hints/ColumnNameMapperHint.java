package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.statements.AbstractStatementCreator;
import de.akquinet.jbosscc.guttenbase.tools.CheckSchemaCompatibilityTool;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;

/**
 * Map the way column names of a table are used. Usually you won't need that, but sometimes you want to map the names, e.g. to add `name`
 * backticks, in order to escape special characters.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @gb.ApplicableForSource
 * @gb.ApplicableForTarget
 * @gb.HintUsedBy {@link AbstractStatementCreator} to map column names
 * @gb.HintUsedBy {@link CheckEqualTableDataTool} to map column names
 * @gb.HintUsedBy {@link CheckSchemaCompatibilityTool} to map column names
 *
 * @author M. Dahm
 */
public abstract class ColumnNameMapperHint implements ConnectorHint<ColumnNameMapper> {
	@Override
	public Class<ColumnNameMapper> getConnectorHintType() {
		return ColumnNameMapper.class;
	}
}
