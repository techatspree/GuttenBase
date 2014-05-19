package de.akquinet.jbosscc.guttenbase.defaults.impl;

import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

/**
 * Default implementation just returns the plain column name.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultColumnNameMapper implements ColumnNameMapper
{
  private final CaseConversionMode _caseConversionMode;

  public DefaultColumnNameMapper(final CaseConversionMode caseConversionMode)
  {
    assert caseConversionMode != null : "caseConversionMode != null";
    _caseConversionMode = caseConversionMode;
  }

  public DefaultColumnNameMapper()
  {
    this(CaseConversionMode.NONE);
  }

  @Override
  public String mapColumnName(final ColumnMetaData columnMetaData)
  {
    return _caseConversionMode.convert(columnMetaData.getColumnName());
  }
}
