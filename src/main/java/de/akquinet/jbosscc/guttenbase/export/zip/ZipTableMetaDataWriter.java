package de.akquinet.jbosscc.guttenbase.export.zip;

import java.io.IOException;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Write ZIP file entry containing information about a table such as name and row count.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ZipTableMetaDataWriter extends ZipAbstractMetaDataWriter {
  public static final String TABLE_NAME = "Name";
  public static final String COLUMN_COUNT = "Column-Count";
  public static final String ROW_COUNT = "Row-Count";

  public ZipTableMetaDataWriter writeTableMetaDataEntry(final TableMetaData tableMetaData) throws IOException {
    setProperty(TABLE_NAME, tableMetaData.getTableName());
    setProperty(COLUMN_COUNT, String.valueOf(tableMetaData.getColumnCount()));
    setProperty(ROW_COUNT, String.valueOf(tableMetaData.getRowCount()));
    return this;
  }
}
