package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.tools.AbstractTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.CheckSchemaCompatibilityTool;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;

/**
 * Map tables between source and target
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @Applicable-For-Target
 * @Hint-Used-By {@link CheckEqualTableDataTool} to get according table
 * @Hint-Used-By {@link CheckSchemaCompatibilityTool} to get according table
 * @Hint-Used-By {@link AbstractTableCopyTool} to get according table
 *
 * @author M. Dahm
 */
public abstract class TableMapperHint implements ConnectorHint<TableMapper> {
	@Override
	public Class<TableMapper> getConnectorHintType() {
		return TableMapper.class;
	}
}
