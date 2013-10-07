package de.akquinet.jbosscc.guttenbase.export;

import java.util.ArrayList;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
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
  private final List<String> _columns = new ArrayList<String>();
  private final List<String> _columnTypes = new ArrayList<String>();

  public ExportTableHeaderImpl(final TableMetaData tableMetaData)
  {
    _tableName = tableMetaData.getTableName();

    for (final ColumnMetaData columnMetaData : tableMetaData.getColumnMetaData())
    {
      _columns.add(columnMetaData.getColumnClassName());
      _columnTypes.add(columnMetaData.getColumnTypeName());
    }
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

  @Override
  public List<String> getColumns()
  {
    return _columns;
  }

  @Override
  public List<String> getColumnTypes()
  {
    return _columnTypes;
  }
}
