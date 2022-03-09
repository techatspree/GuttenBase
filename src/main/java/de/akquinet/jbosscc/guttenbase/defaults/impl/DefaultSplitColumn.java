package de.akquinet.jbosscc.guttenbase.defaults.impl;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.tools.SplitColumn;

import java.util.List;

/**
 * Sometimes the amount of data exceeds buffers. In these cases we need to split the data by some given range, usually the primary key.
 * I.e., the data is read in chunks where these chunks are split using the ID column range of values.
 * <p>
 * By default use the first primary key column, if any. Otherwise returns the first column with a numeric type. Otherwise return the first
 * column.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DefaultSplitColumn implements SplitColumn {
  @Override
  public ColumnMetaData getSplitColumn(final TableMetaData table) {
    final List<ColumnMetaData> columns = table.getColumnMetaData();

    for (final ColumnMetaData columnMetaData : columns) {
      if (columnMetaData.isPrimaryKey()) {
        return columnMetaData;
      }
    }

    for (final ColumnMetaData columnMetaData : columns) {
      final String columnClassName = columnMetaData.getColumnClassName();

      if (ColumnType.isSupportedClass(columnClassName)) {
        final ColumnType columnType = ColumnType.valueForClass(columnClassName);

        if (columnType.isNumber()) {
          return columnMetaData;
        }
      }
    }

    return columns.get(0);
  }
}