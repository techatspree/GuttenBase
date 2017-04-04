package de.akquinet.jbosscc.guttenbase.export.zip;

import java.io.IOException;
import java.util.Iterator;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalColumnMetaData;

/**
 * Write ZIP file entry containing information about a table column index such as name and columns.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public class ZipIndexMetaDataWriter extends ZipAbstractMetaDataWriter {
  public static final String INDEX_NAME = "Index-Name";
  public static final String COLUMN = "Index-Column";
  public static final String COLUMN_ID = "Index-Column-Id";
  public static final String ASCENDING = "Ascending";
  public static final String UNIQUE = "Unique";

  public ZipIndexMetaDataWriter writeIndexMetaDataEntry(final IndexMetaData indexMetaData) throws IOException {
    setProperty(INDEX_NAME, indexMetaData.getIndexName());
    setProperty(ASCENDING, String.valueOf(indexMetaData.isAscending()));
    setProperty(UNIQUE, String.valueOf(indexMetaData.isUnique()));

    int i = 1;
    for (final Iterator<ColumnMetaData> iterator = indexMetaData.getColumnMetaData().iterator(); iterator.hasNext(); i++) {
      final InternalColumnMetaData columnMetaData = (InternalColumnMetaData) iterator.next();

      setProperty(COLUMN_ID + i, String.valueOf(columnMetaData.getColumnId()));
      setProperty(COLUMN + i, columnMetaData.getColumnName());
    }

    return this;
  }
}
