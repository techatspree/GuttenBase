package de.akquinet.jbosscc.guttenbase.meta;

import java.io.Serializable;
import java.util.List;

/**
 * Information about a table.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface TableMetaData extends Comparable<TableMetaData>, Serializable {
  int getRowCount();

  List<ColumnMetaData> getColumnMetaData();

  ColumnMetaData getColumnMetaData(String columnName);

  int getColumnCount();

  String getTableName();

  /**
   * @return containing data base
   */
  DatabaseMetaData getDatabaseMetaData();

  IndexMetaData getIndexMetaData(String indexName);

  List<ColumnMetaData> getPrimaryKeyColumns();

  List<IndexMetaData> getIndexes();

  List<IndexMetaData> getIndexesForColumn(ColumnMetaData columnMetaData);

  List<ForeignKeyMetaData> getForeignKeys();
}
