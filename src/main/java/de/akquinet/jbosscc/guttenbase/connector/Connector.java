package de.akquinet.jbosscc.guttenbase.connector;

import java.sql.Connection;
import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;

/**
 * Connectors maintain informations about the data base and how open and close the SQL {@link Connection}s.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface Connector {
	/**
	 * Open connection or return existing connection
	 */
	Connection openConnection() throws SQLException;

	/**
	 * Close connection if it exists and is open
	 */
	void closeConnection() throws SQLException;

	/**
	 * Return information about database and tables
	 */
	DatabaseMetaData retrieveDatabaseMetaData() throws SQLException;
}