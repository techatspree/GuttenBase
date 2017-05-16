package de.akquinet.jbosscc.guttenbase.repository.impl;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeResolver;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;

/**
 * Will check column type names and determine what Java type is appropriate using some heuristic tests.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class HeuristicColumnTypeResolver implements ColumnTypeResolver {
  /**
   * Performs some heuristic checks on given column type.
   */
  @Override
  public ColumnType getColumnType(final ColumnMetaData columnMetaData) throws SQLException {
    final String columnType = columnMetaData.getColumnTypeName().toUpperCase();
    final DatabaseType databaseType = columnMetaData.getTableMetaData().getDatabaseMetaData().getDatabaseType();

    final ColumnType result = checkDatabaseSpecificTypes(columnType, databaseType);

    if (result == null) {
      if (columnType.endsWith("CHAR") || columnType.endsWith("TEXT") || columnType.startsWith("CHAR")) {
        return ColumnType.CLASS_STRING;
      } else if ("BIGINT".equals(columnType)) {
        return ColumnType.CLASS_LONG;
      } else if (columnType.startsWith("NUMERIC") || "DECIMAL".equals(columnType)) {
        return ColumnType.CLASS_BIGDECIMAL;
      } else if ("INT2".equals(columnType) || "SMALLINT".equals(columnType)) {
        return ColumnType.CLASS_SHORT;
      } else if (columnType.startsWith("INT") || columnType.endsWith("INT") || columnType.equals("COUNTER")) {
        return ColumnType.CLASS_INTEGER;
      } else if (columnType.endsWith("BLOB") || columnType.equals("IMAGE")) {
        return ColumnType.CLASS_BLOB;
      } else if (columnType.equals("BIT") || columnType.startsWith("BOOL")) {
        return ColumnType.CLASS_BOOLEAN;
      } else if (columnType.equals("BYTEA")|| columnType.startsWith("VARBINARY"))  {
        return ColumnType.CLASS_BLOB;
      } else {
        return ColumnType.valueForClass(columnMetaData.getColumnClassName());
      }
    }

    return result;
  }

  private ColumnType checkDatabaseSpecificTypes(final String columnType, final DatabaseType databaseType) {
    switch (databaseType) {
    case POSTGRESQL:
        switch (columnType) {
            case "BIT":
                return ColumnType.CLASS_STRING;
            case "INT8":
                return ColumnType.CLASS_BIGDECIMAL;
            case "OID":
                return ColumnType.CLASS_BLOB;
        }
      break;

    case ORACLE:
      if (columnType.equals("CLOB")) {
        return ColumnType.CLASS_STRING;
      }
      if (columnType.equals("TIMESTAMP")) {
        return ColumnType.CLASS_TIMESTAMP;
      }
      if (columnType.equals("XMLTYPE")) {
        return ColumnType.CLASS_SQLXML;
      }
      break;

    case H2DB:
      if (columnType.equals("CLOB")) {
        return ColumnType.CLASS_STRING;
      }
      break;

    default:
      break;
    }

    return null;
  }
}
