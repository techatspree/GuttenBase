package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.tools.AbstractTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.NumberOfRowsPerBatch;
import java.sql.PreparedStatement;

/**
 * How many rows will be inserted in single transaction? This is an important performance issue.
 *
 * We prefer to use use multiple VALUES() clauses for an INSERT statement in order to insert many rows in one batch. This is much faster in
 * most cases than using {@link PreparedStatement#addBatch()}. Unfortunately, not all databases support multiple VALUES() clauses, so the
 * value must be 1 then.
 *
 * The value also must not be too high so that data buffers are not exceeded.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 *
 * Hint is used by {@link AbstractTableCopyTool} to determine number of VALUES clauses in INSERT statement or statements in batch update
 * @author M. Dahm
 */
public abstract class NumberOfRowsPerBatchHint implements ConnectorHint<NumberOfRowsPerBatch> {
	@Override
	public final Class<NumberOfRowsPerBatch> getConnectorHintType() {
		return NumberOfRowsPerBatch.class;
	}
}
