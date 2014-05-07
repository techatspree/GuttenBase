package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultDatabaseColumnFilter;
import de.akquinet.jbosscc.guttenbase.hints.DatabaseColumnFilterHint;
import de.akquinet.jbosscc.guttenbase.repository.DatabaseColumnFilter;

/**
 * Default implementation will accept any column.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultDatabaseColumnFilterHint extends DatabaseColumnFilterHint
{
  @Override
  public DatabaseColumnFilter getValue()
  {
    return new DefaultDatabaseColumnFilter();
  }
}
