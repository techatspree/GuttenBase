package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.tools.AbstractTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;

/**
 * Map tables between source and target
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 *
 * Hint is used by {@link CheckEqualTableDataTool} to get according table
 * Hint is used by {@link SchemaComparatorTool} to get according table
 * Hint is used by {@link AbstractTableCopyTool} to get according table
 *
 * @author M. Dahm
 */
@SuppressWarnings("deprecation")
public abstract class TableMapperHint implements ConnectorHint<TableMapper> {
	@Override
	public Class<TableMapper> getConnectorHintType() {
		return TableMapper.class;
	}
}
