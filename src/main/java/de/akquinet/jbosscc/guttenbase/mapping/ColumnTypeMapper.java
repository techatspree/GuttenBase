package de.akquinet.jbosscc.guttenbase.mapping;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import java.sql.SQLException;

/**
 * Sometimes data types of columns are not compatible: Allow user to set mapping.
 */
public interface ColumnTypeMapper
{
  String mapColumnType(final ColumnMetaData columnMetaData, final DatabaseType sourceDatabase, final DatabaseType targetDatabase)
    throws SQLException;
}
