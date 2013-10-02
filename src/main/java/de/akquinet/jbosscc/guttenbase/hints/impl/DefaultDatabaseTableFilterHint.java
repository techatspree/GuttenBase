package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.DatabaseTableFilterHint;
import de.akquinet.jbosscc.guttenbase.repository.DatabaseTableFilter;

/**
 * Default implementation will accept any table.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultDatabaseTableFilterHint extends DatabaseTableFilterHint {
  @Override
  public DatabaseTableFilter getValue() {
    return new DefaultDatabaseTableFilter();
  }
}
