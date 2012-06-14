package de.akquinet.jbosscc.guttenbase.hints;

import java.sql.PreparedStatement;

import de.akquinet.jbosscc.guttenbase.tools.AbstractTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.NumberOfRowsPerInsertion;

/**
 * How many rows will be inserted in single transaction? This is an important performance issue.
 * 
 * We use multiple VALUES() clauses for an INSERT statement in order to insert many rows in one batch. This is much faster in most cases
 * than using {@link PreparedStatement#addBatch()}. Unfortunately, not all databases support multiple VALUES() clauses, so the value must be
 * 1 then.
 * 
 * The value also must not be too high so that data buffers are not exceeded.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @Applicable-For-Target
 * @Hint-Used-By {@link AbstractTableCopyTool} to determine number of VALUES clauses in INSERT statement or statements in batch update
 * @author M. Dahm
 */
public abstract class NumberOfRowsPerInsertionHint implements ConnectorHint<NumberOfRowsPerInsertion> {
	@Override
	public final Class<NumberOfRowsPerInsertion> getConnectorHintType() {
		return NumberOfRowsPerInsertion.class;
	}
}
