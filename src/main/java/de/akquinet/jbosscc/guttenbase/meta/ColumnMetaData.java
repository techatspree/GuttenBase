package de.akquinet.jbosscc.guttenbase.meta;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Information about a table column.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public interface ColumnMetaData extends Comparable<ColumnMetaData>, Serializable
{
  int getColumnType();

  String getColumnName();

  String getColumnTypeName();

  String getColumnClassName();

  /**
   * @return containing table
   */
  TableMetaData getTableMetaData();

  boolean isNullable();

  boolean isAutoIncrement();

  int getPrecision();

  int getScale();

  boolean isPrimaryKey();

  /**
   * @return referenced columns for each foreign key constraint
   */
  Map<String, List<ColumnMetaData>> getReferencedColumns();

  /**
   * @return list of referencing columns for each foreign key constraint
   */
  Map<String, List<ColumnMetaData>> getReferencingColumns();
}
