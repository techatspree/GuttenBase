package de.akquinet.jbosscc.guttenbase.export.zip;

import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Write ZIP file entry containing information about data base such as schema or version.
 * <p></p>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public class ZipDatabaseMetaDataWriter extends ZipAbstractMetaDataWriter {
  public static final String DATABASE_NAME = "Database";
  public static final String DATABASE_TYPE = "Database-Type";
  public static final String DATABASE_MAJOR_VERSION = "Major-Version";
  public static final String DATABASE_MINOR_VERSION = "Minor-Version";
  public static final String DATABASE_SCHEMA = "Database-Schema";
  public static final String TABLE_NAME = "Table-Name";

  public ZipDatabaseMetaDataWriter writeDatabaseMetaDataEntry(final DatabaseMetaData databaseMetaData) throws IOException, SQLException {
    setProperty(DATABASE_SCHEMA, databaseMetaData.getSchema());
    setProperty(DATABASE_NAME, databaseMetaData.getDatabaseMetaData().getDatabaseProductName());
    setProperty(DATABASE_MAJOR_VERSION, String.valueOf(databaseMetaData.getDatabaseMetaData().getDatabaseMajorVersion()));
    setProperty(DATABASE_MINOR_VERSION, String.valueOf(databaseMetaData.getDatabaseMetaData().getDatabaseMinorVersion()));
    setProperty(DATABASE_TYPE, databaseMetaData.getDatabaseType().name());

    int i = 1;
    for (final Iterator<TableMetaData> iterator = databaseMetaData.getTableMetaData().iterator(); iterator.hasNext(); i++) {
      final TableMetaData tableMetaData = iterator.next();
      setProperty(TABLE_NAME + i, tableMetaData.getTableName());
    }

    return this;
  }
}
