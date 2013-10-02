package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.TableNameMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * By default prepend schema name.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultTableNameMapperHint extends TableNameMapperHint {
	@Override
	public TableNameMapper getValue() {
		return new TableNameMapper() {
			@Override
			public String mapTableName(final TableMetaData tableMetaData) {
				final String schema = tableMetaData.getDatabaseMetaData().getSchema();
				final String table = tableMetaData.getTableName();

				if ("".equals(schema.trim())) {
					return table;
				} else {
					return schema + "." + table;
				}
			}
		};
	}
}
