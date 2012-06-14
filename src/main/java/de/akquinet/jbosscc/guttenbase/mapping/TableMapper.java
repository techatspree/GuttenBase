package de.akquinet.jbosscc.guttenbase.mapping;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Select target table for given source table.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface TableMapper {
	/**
	 * Return matching table in target data base or null
	 */
	TableMetaData map(TableMetaData source, DatabaseMetaData targetDatabaseMetaData) throws SQLException;
}
