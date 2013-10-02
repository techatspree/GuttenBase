package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.ColumnNameMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

/**
 * Default implementation just returns the plain column name.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultColumnNameMapperHint extends ColumnNameMapperHint {
	@Override
	public ColumnNameMapper getValue() {
		return new ColumnNameMapper() {
			@Override
			public String mapColumnName(final ColumnMetaData columnMetaData) {
				return columnMetaData.getColumnName();
			}
		};
	}
}
