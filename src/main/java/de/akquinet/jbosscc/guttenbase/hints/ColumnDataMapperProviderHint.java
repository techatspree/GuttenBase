package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnDataMapperProvider;
import de.akquinet.jbosscc.guttenbase.tools.CommonColumnTypeResolverTool;

/**
 * Used to find mappings for column data. E.g., when converting a number to a String or casting a LONG to a BIGINT.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * Hint is used by {@link CommonColumnTypeResolverTool} to determine mapping between different column types
 *
 * @author M. Dahm
 */
public abstract class ColumnDataMapperProviderHint implements ConnectorHint<ColumnDataMapperProvider> {
	@Override
	public Class<ColumnDataMapperProvider> getConnectorHintType() {
		return ColumnDataMapperProvider.class;
	}
}
