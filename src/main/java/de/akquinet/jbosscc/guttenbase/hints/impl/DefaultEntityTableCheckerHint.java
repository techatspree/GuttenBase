package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.EntityTableCheckerHint;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.tools.EntityTableChecker;

/**
 * By default we check if the given table has an primary key column named "ID".
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultEntityTableCheckerHint extends EntityTableCheckerHint {
	@Override
	public EntityTableChecker getValue() {
		return new EntityTableChecker() {
			@Override
			public boolean isEntityTable(final TableMetaData tableMetaData) {
				for (final ColumnMetaData columnMetaData : tableMetaData.getColumnMetaData()) {
					if (columnMetaData.isPrimaryKey() && columnMetaData.getColumnName().equalsIgnoreCase("ID")) {
						return true;
					}
				}

				return false;
			}
		};
	}
}
