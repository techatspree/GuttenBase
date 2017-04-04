package de.akquinet.jbosscc.guttenbase.mapping;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;

/**
 * Used to find mappings for column data. E.g., when converting a number to a String or casting a LONG to a BIGINT.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public interface ColumnDataMapperProvider
{
  /**
   * Find mapping the given configuration.
   * 
   * @param sourceColumnMetaData
   *          source column
   * @param targetColumnMetaData
   *          target column
   * @param sourceColumnType
   *          as determined by {@link ColumnTypeResolver}
   * @param targetColumnType
   *          as determined by {@link ColumnTypeResolver}
   */
  ColumnDataMapper findMapping(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData,
      final ColumnType sourceColumnType, final ColumnType targetColumnType) throws SQLException;

  /**
   * Specify additional mapping
   */
  void addMapping(final ColumnType sourceColumnType, final ColumnType targetColumnType, final ColumnDataMapper columnDataMapper)
      throws SQLException;
}
