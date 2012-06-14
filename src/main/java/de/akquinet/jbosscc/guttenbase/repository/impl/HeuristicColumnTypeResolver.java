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
 * &copy; 2012 akquinet tech@spree
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

		if (columnType.endsWith("CHAR") || columnType.endsWith("TEXT") || columnType.startsWith("CHAR")
				|| (columnType.equals("BIT") && databaseType.equals(DatabaseType.POSTGRESQL))) {
			return ColumnType.CLASS_STRING;
		} else if ("INT8".equals(columnType) || columnType.startsWith("NUMERIC") || "DECIMAL".equals(columnType)
				|| columnType.startsWith("BIGINT")) {
			return ColumnType.CLASS_BIGDECIMAL;
		} else if ("INT2".equals(columnType) || "SMALLINT".equals(columnType)) {
			return ColumnType.CLASS_SHORT;
		} else if (columnType.startsWith("INT") || columnType.endsWith("INT") || columnType.equals("COUNTER")) {
			return ColumnType.CLASS_INTEGER;
		} else if (columnType.endsWith("OID") || columnType.endsWith("BLOB") || columnType.equals("IMAGE")) {
			return ColumnType.CLASS_BLOB;
		} else if (columnType.equals("BIT") || columnType.startsWith("BOOL")) {
			return ColumnType.CLASS_BOOLEAN;
		} else if (columnType.equals("BYTEA")) {
			return ColumnType.CLASS_OBJECT;
		} else {
			return ColumnType.valueForClass(columnMetaData.getColumnClassName());
		}
	}
}
