package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.configuration.impl.MsSqlTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.PostgresqlTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.tools.AbstractSequenceUpdateTool;
import de.akquinet.jbosscc.guttenbase.tools.AbstractTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.MinMaxIdSelectorTool;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;

/**
 * Map table names, e.g. prepend schema name schema.table or add backticks (`) to escape special names.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @Applicable-For-Source
 * @Applicable-For-Target
 * @Hint-Used-By {@link MsSqlTargetDatabaseConfiguration}
 * @Hint-Used-By {@link PostgresqlTargetDatabaseConfiguration}
 * @Hint-Used-By {@link AbstractSequenceUpdateTool}
 * @Hint-Used-By {@link AbstractTableCopyTool}
 * @Hint-Used-By {@link MinMaxIdSelectorTool}
 * @Hint-Used-By {@link CheckEqualTableDataTool}
 * 
 * @author M. Dahm
 */
public abstract class TableNameMapperHint implements ConnectorHint<TableNameMapper> {
	@Override
	public Class<TableNameMapper> getConnectorHintType() {
		return TableNameMapper.class;
	}
}
