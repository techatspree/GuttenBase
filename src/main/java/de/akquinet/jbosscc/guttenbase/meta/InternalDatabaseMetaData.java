package de.akquinet.jbosscc.guttenbase.meta;

/**
 * Extension for internal access.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface InternalDatabaseMetaData extends DatabaseMetaData {
	void addTableMetaData(final TableMetaData tableMetaData);

	void clearTableData();
}