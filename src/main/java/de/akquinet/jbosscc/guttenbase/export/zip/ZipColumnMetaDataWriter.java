package de.akquinet.jbosscc.guttenbase.export.zip;

import java.io.IOException;
import java.util.Iterator;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalColumnMetaData;

/**
 * Write ZIP file entry containing information about a table column such as type and name.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public class ZipColumnMetaDataWriter extends ZipAbstractMetaDataWriter {
  public static final String COLUMN_NAME = "Name";
  public static final String COLUMN_ID = "Column-Id";
  public static final String COLUMN_CLASS_NAME = "Class-Name";
  public static final String COLUMN_TYPE_NAME = "Type-Name";
  public static final String COLUMN_TYPE = "JDBC-Type";
  public static final String COLUMN_PRECISION = "Precision";
  public static final String COLUMN_SCALE = "Scale";
  public static final String COLUMN_REFERENCES_ID = "References-Id";
  public static final String COLUMN_REFERENCES = "References";
  public static final String COLUMN_REFERENCED_BY_ID = "Referenced-By-Id";
  public static final String COLUMN_REFERENCED_BY = "Referenced-By";
  public static final String PRIMARY_KEY = "Primary-Key";
  public static final String NULLABLE = "Nullable";
  public static final String AUTO_INCREMENT = "Auto-Increment";

  public ZipColumnMetaDataWriter writeColumnMetaDataEntry(final ColumnMetaData columnMetaData) throws IOException {
    setProperty(COLUMN_NAME, columnMetaData.getColumnName());
    setProperty(COLUMN_CLASS_NAME, columnMetaData.getColumnClassName());
    setProperty(COLUMN_TYPE_NAME, columnMetaData.getColumnTypeName());
    setProperty(COLUMN_TYPE, String.valueOf(columnMetaData.getColumnType()));
    setProperty(COLUMN_PRECISION, String.valueOf(columnMetaData.getPrecision()));
    setProperty(COLUMN_PRECISION, String.valueOf(columnMetaData.getPrecision()));
    setProperty(COLUMN_SCALE, String.valueOf(columnMetaData.getScale()));
    setProperty(PRIMARY_KEY, String.valueOf(columnMetaData.isPrimaryKey()));
    setProperty(NULLABLE, String.valueOf(columnMetaData.isNullable()));
    setProperty(AUTO_INCREMENT, String.valueOf(columnMetaData.isAutoIncrement()));
    setProperty(COLUMN_ID, String.valueOf(((InternalColumnMetaData) columnMetaData).getColumnId()));

    final InternalColumnMetaData referencedColumn = (InternalColumnMetaData) columnMetaData.getReferencedColumn();

    if (referencedColumn != null) {
      setProperty(COLUMN_REFERENCES_ID, String.valueOf(referencedColumn.getColumnId()));
      setProperty(COLUMN_REFERENCES, referencedColumn.getColumnName() + " (" + referencedColumn.getTableMetaData().getTableName() + ")");
    }

    int i = 1;
    for (final Iterator<ColumnMetaData> iterator = columnMetaData.getReferencedByColumn().iterator(); iterator.hasNext(); i++) {
      final InternalColumnMetaData referencedByColumn = (InternalColumnMetaData) iterator.next();
      setProperty(COLUMN_REFERENCED_BY_ID + i, String.valueOf(referencedByColumn.getColumnId()));
      setProperty(COLUMN_REFERENCED_BY + i, referencedByColumn.getColumnName() + " (" + referencedByColumn.getTableMetaData().getTableName() + ")");
    }

    return this;
  }
}
