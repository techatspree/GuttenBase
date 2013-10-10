package de.akquinet.jbosscc.guttenbase.tools.schema;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

/**
 * Sometimes data types of columns are not compatible: Allow user to set mapping.
 */
public interface SchemaColumnTypeMapper
{
  String getColumnType(final ColumnMetaData columnMetaData);
}
