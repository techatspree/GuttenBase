package de.akquinet.jbosscc.guttenbase.meta;

import java.io.Serializable;
import java.util.List;

/**
 * Information about index in table.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface IndexMetaData extends Serializable
{
  String getIndexName();

  boolean isAscending();

  boolean isUnique();

  List<ColumnMetaData> getColumnMetaData();

  TableMetaData getTableMetaData();

  boolean isPrimaryKeyIndex();
}
