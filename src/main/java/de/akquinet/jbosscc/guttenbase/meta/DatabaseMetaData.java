package de.akquinet.jbosscc.guttenbase.meta;

import java.io.Serializable;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryTableFilter;

/**
 * Information about data base such as schema name.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface DatabaseMetaData extends Serializable {
  String getSchema();

  String getDatabaseName();

  int getMajorVersion();

  int getMinorVersion();

  /**
   * Return tables list filtered by @see {@link RepositoryTableFilter}
   */
  List<TableMetaData> getTableMetaData();

  TableMetaData getTableMetaData(String tableName);

  DatabaseType getDatabaseType();
}
