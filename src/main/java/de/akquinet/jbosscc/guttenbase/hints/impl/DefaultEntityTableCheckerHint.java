package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.EntityTableCheckerHint;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.tools.EntityTableChecker;

/**
 * By default we check if the given table has an primary key column named "ID".
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultEntityTableCheckerHint extends EntityTableCheckerHint {
	@Override
	public EntityTableChecker getValue() {
		return tableMetaData -> {
            for (final ColumnMetaData columnMetaData : tableMetaData.getColumnMetaData()) {
                final String columnName = columnMetaData.getColumnName().toUpperCase();

                if (columnMetaData.isPrimaryKey() && (columnName.equals("ID") || columnName.equals("IDENT"))) {
                    return true;
                }
            }

            return false;
        };
	}
}
