package de.akquinet.jbosscc.guttenbase.configuration;

import java.sql.Connection;
import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Implementations may execute specific initialization code before and after operations are executed.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface DatabaseConfiguration {
	/**
	 * Called before table is copied
	 */
	void beforeTableCopy(final Connection connection, String connectorId, TableMetaData table) throws SQLException;

	/**
	 * Called after table has been copied
	 */
	void afterTableCopy(final Connection connection, String connectorId, TableMetaData table) throws SQLException;
}
