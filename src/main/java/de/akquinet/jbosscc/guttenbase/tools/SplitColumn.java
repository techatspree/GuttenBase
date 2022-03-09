package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Sometimes the amount of data exceeds any buffer. In these cases we need to split the data by some given range, usually the primary key.
 * I.e., the data is read in chunks where these chunks are split using the ID column range of values.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public interface SplitColumn {
  /**
   * @return column of the table, i.e. in general the primary key or any other column name if no primary key column is appropriate
   */
  ColumnMetaData getSplitColumn(TableMetaData tableName);
}
