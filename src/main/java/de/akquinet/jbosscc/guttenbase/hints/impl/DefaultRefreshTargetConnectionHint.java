package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.RefreshTargetConnectionHint;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.tools.RefreshTargetConnection;

/**
 * By default, the connection is never flushed.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DefaultRefreshTargetConnectionHint extends RefreshTargetConnectionHint {
  @Override
  public RefreshTargetConnection getValue() {
    return new RefreshTargetConnection() {
      @Override
      public boolean refreshConnection(int noCopiedTables, TableMetaData sourceTableMetaData) {
        return false;
      }
    };
  }
}
