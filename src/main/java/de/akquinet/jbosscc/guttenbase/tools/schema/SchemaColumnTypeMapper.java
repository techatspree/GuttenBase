package de.akquinet.jbosscc.guttenbase.tools.schema;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

public interface SchemaColumnTypeMapper {
	String getColumnType(final ColumnMetaData columnMetaData);
}