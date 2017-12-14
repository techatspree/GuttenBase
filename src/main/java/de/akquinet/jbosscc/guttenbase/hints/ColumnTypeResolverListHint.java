package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeResolverList;
import de.akquinet.jbosscc.guttenbase.repository.impl.ClassNameColumnTypeResolver;
import de.akquinet.jbosscc.guttenbase.repository.impl.HeuristicColumnTypeResolver;
import de.akquinet.jbosscc.guttenbase.tools.CommonColumnTypeResolverTool;

/**
 * Determine strategies to use for mapping different column types. It provides a list of column type resolvers which will be asked in turn
 * to resolve a column type conflict.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 *
 * Hint is used by {@link CommonColumnTypeResolverTool} to determine mapping strategies between different column types
 *
 * @see ClassNameColumnTypeResolver
 * @see HeuristicColumnTypeResolver
 * @author M. Dahm
 */
public abstract class ColumnTypeResolverListHint implements ConnectorHint<ColumnTypeResolverList> {
	@Override
	public Class<ColumnTypeResolverList> getConnectorHintType() {
		return ColumnTypeResolverList.class;
	}
}
