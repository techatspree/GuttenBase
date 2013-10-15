package de.akquinet.jbosscc.guttenbase.tools.schema;

import java.sql.Types;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

/**
 * Default uses same data type as source
 * 
 * @copyright akquinet tech@spree GmbH, 2002-2020
 */
public class DefaultSchemaColumnTypeMapper implements SchemaColumnTypeMapper
{
  @Override
  public String getColumnType(final ColumnMetaData columnMetaData)
  {
    final String precision = createPrecisionClause(columnMetaData);
    return columnMetaData.getColumnTypeName() + precision;
  }

  private String createPrecisionClause(final ColumnMetaData columnMetaData)
  {
    switch (columnMetaData.getColumnType())
    {
    case Types.CHAR:
    case Types.VARCHAR:
    case Types.VARBINARY:
      return "(" + columnMetaData.getPrecision() + ")";

    default:
      return "";
    }
  }
}
