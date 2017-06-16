package de.akquinet.jbosscc.guttenbase.meta;

import java.io.Serializable;
import java.util.List;

/**
 * Information about a table.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public interface TableMetaData extends Comparable<TableMetaData>, Serializable
{
  int getFilteredRowCount();

  int getTotalRowCount();

  List<ColumnMetaData> getColumnMetaData();

  ColumnMetaData getColumnMetaData(String columnName);

  int getColumnCount();

  /**
   * @return type such as "TABLE" or "VIEW"
   */
  String getTableType();

  String getTableName();

  /**
   * @return containing data base
   */
  DatabaseMetaData getDatabaseMetaData();

  IndexMetaData getIndexMetaData(String indexName);

  List<ColumnMetaData> getPrimaryKeyColumns();

  List<IndexMetaData> getIndexes();

  List<IndexMetaData> getIndexesContainingColumn(ColumnMetaData columnMetaData);

  List<ForeignKeyMetaData> getImportedForeignKeys();

  List<ForeignKeyMetaData> getExportedForeignKeys();
}
