package de.akquinet.jbosscc.guttenbase.export.zip;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalColumnMetaData;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Write ZIP file entry containing information about a table column such as type and name.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
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
  public static final String COLUMN_FK_REFERENCES_IDS_SUFFIX = ".References-Ids";
  public static final String COLUMN_FK_REFERENCES_SUFFIX = "References";
  public static final String COLUMN_FK_REFERENCED_BY_IDS_SUFFIX = "Referenced-By-Ids";
  public static final String COLUMN_FK_REFERENCED_BY_SUFFIX = "Referenced-By";
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

    setFkProperties(columnMetaData.getReferencedColumns(), COLUMN_FK_REFERENCES_IDS_SUFFIX, COLUMN_FK_REFERENCES_SUFFIX);
    setFkProperties(columnMetaData.getReferencingColumns(), COLUMN_FK_REFERENCED_BY_IDS_SUFFIX, COLUMN_FK_REFERENCED_BY_SUFFIX);

    return this;
  }

  private void setFkProperties(final Map<String, List<ColumnMetaData>> fkColumns, final String columnFkReferencesIdsSuffix,
                               final String columnFkReferencesSuffix) {
    for (final Map.Entry<String, List<ColumnMetaData>> entry : fkColumns.entrySet()) {
      final List<ColumnMetaData> columns = entry.getValue();
      final String ids = mapToIds(columns);
      final String names = mapToNames(columns);

      setProperty(entry.getKey() + "." + columnFkReferencesIdsSuffix, ids);
      setProperty(entry.getKey() + "." + columnFkReferencesSuffix, names);
    }
  }

  private static String mapToNames(final List<ColumnMetaData> columns) {
    return columns.stream()
        .map(referencedColumn -> referencedColumn.getColumnName()
            + " ("
            + referencedColumn.getTableMetaData().getTableName()
            + ")")
        .collect(Collectors.joining(", "));
  }

  private static String mapToIds(final List<ColumnMetaData> columns) {
    return columns.stream()
        .map(referencedColumn -> String.valueOf(((InternalColumnMetaData) referencedColumn).getColumnId()))
        .collect(Collectors.joining(", "));
  }
}
