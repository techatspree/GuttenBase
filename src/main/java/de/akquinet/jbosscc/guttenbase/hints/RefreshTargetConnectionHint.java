package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.tools.AbstractTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.RefreshTargetConnection;

/**
 * Some JDBC drivers seem to accumulate data over time, even after a connection is commited() and all statements, result sets, etc. are closed.
 * This will cause an OutOfMemoryError eventually.<br>
 * To avoid this the connection can be flushed, closed and re-established periodically using this hint.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 *
 * Hint is used by {@link AbstractTableCopyTool} to determine table order
 */
public abstract class RefreshTargetConnectionHint implements ConnectorHint<RefreshTargetConnection> {
  @Override
  public Class<RefreshTargetConnection> getConnectorHintType() {
    return RefreshTargetConnection.class;
  }

}
