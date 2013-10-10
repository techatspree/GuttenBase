package de.akquinet.jbosscc.guttenbase.tools.schema;

import java.sql.Types;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

public class DefaultSchemaColumnTypeMapper implements
		SchemaColumnTypeMapper {
	@Override
	public String getColumnType(ColumnMetaData columnMetaData) {
		final String precision = createPrecisionClause(columnMetaData);
		return columnMetaData.getColumnTypeName() + precision;
	}

	private String createPrecisionClause(final ColumnMetaData columnMetaData) {
		switch (columnMetaData.getColumnType()) {
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.VARBINARY:
			return "(" + columnMetaData.getPrecision() + ")";

		default:
			return "";
		}
	}
}