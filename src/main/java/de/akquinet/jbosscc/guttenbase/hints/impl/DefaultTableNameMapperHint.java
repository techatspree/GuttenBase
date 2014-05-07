package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.TableNameMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;

/**
 * By default prepend schema name.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultTableNameMapperHint extends TableNameMapperHint
{
  @Override
  public TableNameMapper getValue()
  {
    return new DefaultTableNameMapper();
  }
}
