package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.tools.AbstractTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.ResultSetParameters;

/**
 * <p>Set fetch size, result set type and concurrency tye for result set,
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 *
 * Hint is used by {@link AbstractTableCopyTool} to determine number of VALUES clauses in INSERT statement or statements in batch update
 */
public abstract class ResultSetParametersHint implements ConnectorHint<ResultSetParameters> {
  @Override
  public final Class<ResultSetParameters> getConnectorHintType() {
    return ResultSetParameters.class;
  }
}
