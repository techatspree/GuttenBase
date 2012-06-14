package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.statements.SplitByColumnSelectCountStatementCreator;
import de.akquinet.jbosscc.guttenbase.statements.SplitByColumnSelectMinMaxStatementCreator;
import de.akquinet.jbosscc.guttenbase.statements.SplitByColumnSelectStatementCreator;
import de.akquinet.jbosscc.guttenbase.tools.SplitByRangeTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.SplitColumn;

/**
 * Sometimes the amount of data exceeds buffers. In these cases we need to split the read data by some given range, usually the primary key.
 * I.e., the data is read in chunks where these chunks are split using the ID column range of values.
 * 
 * With this hint one may configure the column to be used for splitting.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @Applicable-For-Source
 * @Hint-Used-By {@link SplitByColumnSelectCountStatementCreator}
 * @Hint-Used-By {@link SplitByColumnSelectMinMaxStatementCreator}
 * @Hint-Used-By {@link SplitByColumnSelectStatementCreator}
 * @Hint-Used-By {@link SplitByRangeTableCopyTool}
 * 
 * @author M. Dahm
 */
public abstract class SplitColumnHint implements ConnectorHint<SplitColumn> {
	@Override
	public final Class<SplitColumn> getConnectorHintType() {
		return SplitColumn.class;
	}
}
