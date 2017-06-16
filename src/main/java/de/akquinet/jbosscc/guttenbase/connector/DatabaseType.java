package de.akquinet.jbosscc.guttenbase.connector;

/**
 * Denote known/handled data bases. Easy to extend...
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public enum DatabaseType {
	GENERIC, MOCK, EXPORT_DUMP, IMPORT_DUMP, MYSQL, POSTGRESQL, MSSQL, MS_ACCESS, HSQLDB, H2DB, DERBY,
	DB2, SYBASE, ORACLE
}
