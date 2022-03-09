package de.akquinet.jbosscc.guttenbase.mapping;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;

/**
 * Map column types to Java types. Every data base has its own special types which have to be mapped to standard types somehow.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public interface ColumnTypeResolver {
  /**
   * Tries to map column to a known type.
   */
  ColumnType getColumnType(ColumnMetaData columnMetaData);
}