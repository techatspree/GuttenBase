package de.akquinet.jbosscc.guttenbase.hints.impl;

import java.util.Arrays;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.hints.ColumnTypeResolverListHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeResolver;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeResolverList;
import de.akquinet.jbosscc.guttenbase.repository.impl.ClassNameColumnTypeResolver;
import de.akquinet.jbosscc.guttenbase.repository.impl.HeuristicColumnTypeResolver;

/**
 * Default implementation returns {@link ClassNameColumnTypeResolver} and {@link HeuristicColumnTypeResolver}.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultColumnTypeResolverListHint extends ColumnTypeResolverListHint {
	@Override
	public ColumnTypeResolverList getValue() {
		return new ColumnTypeResolverList() {
			@Override
			public List<ColumnTypeResolver> getColumnTypeResolvers() {
				return Arrays.asList(new ClassNameColumnTypeResolver(), new HeuristicColumnTypeResolver());
			}
		};
	}
}
