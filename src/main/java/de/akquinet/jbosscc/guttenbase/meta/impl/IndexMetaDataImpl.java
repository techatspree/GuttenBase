package de.akquinet.jbosscc.guttenbase.meta.impl;

import java.util.ArrayList;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalIndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Information about index in table.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class IndexMetaDataImpl implements InternalIndexMetaData
{
  private static final long serialVersionUID = 1L;

  private final String _indexName;
  private final boolean _ascending;
  private final boolean _unique;
  private final List<ColumnMetaData> _columns = new ArrayList<ColumnMetaData>();
  private final TableMetaData _tableMetaData;
  private final boolean _primaryKeyIndex;

  public IndexMetaDataImpl(
      final TableMetaData tableMetaData,
      final String indexName,
      final boolean ascending,
      final boolean unique,
      final boolean primaryKeyIndex)
  {
    assert indexName != null : "indexName != null";
    assert tableMetaData != null : "tableMetaData != null";

    _tableMetaData = tableMetaData;
    _indexName = indexName;
    _ascending = ascending;
    _unique = unique;
    _primaryKeyIndex = primaryKeyIndex;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TableMetaData getTableMetaData()
  {
    return _tableMetaData;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIndexName()
  {
    return _indexName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAscending()
  {
    return _ascending;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isUnique()
  {
    return _unique;
  }

  @Override
  public final boolean isPrimaryKeyIndex()
  {
    return _primaryKeyIndex;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ColumnMetaData> getColumnMetaData()
  {
    return new ArrayList<ColumnMetaData>(_columns);
  }

  @Override
  public void addColumn(final ColumnMetaData columnMetaData)
  {
    assert columnMetaData != null : "columnMetaData != null";
    _columns.add(columnMetaData);
  }
}
