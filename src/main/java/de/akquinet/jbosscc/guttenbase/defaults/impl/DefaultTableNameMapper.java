package de.akquinet.jbosscc.guttenbase.defaults.impl;

import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * By default prepend schema name.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DefaultTableNameMapper implements TableNameMapper
{
  private final CaseConversionMode _caseConversionMode;
  private final boolean _addSchema;

  public DefaultTableNameMapper(final CaseConversionMode caseConversionMode, final boolean addSchema)
  {
    assert caseConversionMode != null : "caseConversionMode != null";
    _caseConversionMode = caseConversionMode;
    _addSchema = addSchema;
  }

  public DefaultTableNameMapper()
  {
    this(CaseConversionMode.NONE, true);
  }

  @Override
  public String mapTableName(final TableMetaData tableMetaData)
  {
    final String schema = tableMetaData.getDatabaseMetaData().getSchema();
    final String table = _caseConversionMode.convert(tableMetaData.getTableName());

    if ("".equals(schema.trim()) || !_addSchema)
    {
      return table;
    }
    else
    {
      return schema + "." + table;
    }
  }
}
