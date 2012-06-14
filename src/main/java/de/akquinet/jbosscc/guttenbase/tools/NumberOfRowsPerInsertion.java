package de.akquinet.jbosscc.guttenbase.tools;

import java.sql.PreparedStatement;

/**
 * How many rows will be inserted in single transaction? This is an important performance issue.
 * 
 * We support two ways to insert multiple rows in one batch: Either with the {@link PreparedStatement#addBatch()} method or with multiple
 * VALUES() clauses for an INSERT statement. The latter method is much faster in most cases, but not all databases support this, so the
 * value must be 1 then.
 * 
 * The value also must not be too high so data buffers are not exceeded.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @see MaxNumberOfDataItems
 * @author M. Dahm
 */

public interface NumberOfRowsPerInsertion {
  int getNumberOfRowsPerInsertion();

  /**
   * Use VALUES() clauses or {@link PreparedStatement#addBatch()} as discussed above
   */
  boolean useValuesClauses();
}
