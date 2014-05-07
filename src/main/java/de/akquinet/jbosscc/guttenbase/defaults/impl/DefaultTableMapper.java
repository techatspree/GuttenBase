package de.akquinet.jbosscc.guttenbase.defaults.impl;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * By default return table with same name ignoring case.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultTableMapper implements TableMapper {
	@Override
	public TableMetaData map(final TableMetaData source, final DatabaseMetaData targetDatabaseMetaData) throws SQLException {
		final String tableName = source.getTableName();
		return targetDatabaseMetaData.getTableMetaData(tableName);
	}
}
