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
 * @gb.ApplicableForTarget
 * @gb.HintUsedBy {@link CheckEqualTableDataTool} to get according table
 * @gb.HintUsedBy {@link SchemaComparatorTool} to get according table
 * @gb.HintUsedBy {@link AbstractTableCopyTool} to get according table
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
