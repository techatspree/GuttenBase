package de.akquinet.jbosscc.guttenbase.hints.impl;

import java.util.Arrays;

import de.akquinet.jbosscc.guttenbase.hints.ColumnTypeResolverListHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeResolverList;
import de.akquinet.jbosscc.guttenbase.repository.impl.ClassNameColumnTypeResolver;
import de.akquinet.jbosscc.guttenbase.repository.impl.HeuristicColumnTypeResolver;

/**
 * Default implementation tries {@link HeuristicColumnTypeResolver} first, then {@link ClassNameColumnTypeResolver}.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultColumnTypeResolverListHint extends ColumnTypeResolverListHint {
  @Override
  public ColumnTypeResolverList getValue() {
    return () -> Arrays.asList(new HeuristicColumnTypeResolver(), new ClassNameColumnTypeResolver());
  }
}
