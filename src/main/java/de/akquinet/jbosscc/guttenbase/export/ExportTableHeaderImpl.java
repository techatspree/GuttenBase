package de.akquinet.jbosscc.guttenbase.export;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Denote start of new table in export file
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ExportTableHeaderImpl implements ExportTableHeader
{
  private static final long serialVersionUID = 1L;

  private final String _tableName;

  public ExportTableHeaderImpl(final TableMetaData tableMetaData)
  {
    _tableName = tableMetaData.getTableName();
  }

  @Override
  public String getTableName()
  {
    return _tableName;
  }

  @Override
  public String toString()
  {
    return getTableName();
  }
}
