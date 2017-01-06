package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

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
 */

public interface RefreshTargetConnection {
  boolean refreshConnection(int noCopiedTables, TableMetaData sourceTableMetaData);
}
