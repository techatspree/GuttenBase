package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.ColumnTypeMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.DefaultColumnTypeMapper;

/**
 * By default use customized mapping since database column types are sometimes different.
 * <p></p>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DefaultColumnTypeMapperHint extends ColumnTypeMapperHint {
  @Override
  public ColumnTypeMapper getValue() {
    return new DefaultColumnTypeMapper();
  }
}
