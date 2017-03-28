package de.akquinet.jbosscc.guttenbase.meta.builder;

import java.util.ArrayList;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.meta.InternalIndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.meta.impl.IndexMetaDataImpl;

/**
 * Builder class for IndexMetaData.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class IndexMetaDataBuilder
{
  private String _indexName = "IDX";
  private boolean _ascending = false;
  private boolean _unique = false;
  private boolean _primaryKey = false;
  private final List<ColumnMetaDataBuilder> _columns = new ArrayList<>();
  private final TableMetaDataBuilder _tableMetaDataBuilder;
  private IndexMetaDataImpl _result;

  public IndexMetaDataBuilder(final TableMetaDataBuilder tableMetaDataBuilder)
  {
    _tableMetaDataBuilder = tableMetaDataBuilder;
  }

  public InternalIndexMetaData build()
  {
    if (_result == null)
    {
      final TableMetaData table = _tableMetaDataBuilder.build();
      _result = new IndexMetaDataImpl(table, _indexName, _ascending, _unique, _primaryKey);

      for (final ColumnMetaDataBuilder columnMetaDataBuilder : _columns)
      {
        _result.addColumn(table.getColumnMetaData(columnMetaDataBuilder.getColumnName()));
      }
    }

    return _result;
  }

  public IndexMetaDataBuilder setIndexName(final String indexName)
  {
    _indexName = indexName;
    return this;
  }

  public IndexMetaDataBuilder setAscending(final boolean ascending)
  {
    _ascending = ascending;
    return this;
  }

  public final IndexMetaDataBuilder setPrimaryKey(final boolean primaryKey)
  {
    _primaryKey = primaryKey;
    return this;
  }

  public IndexMetaDataBuilder setUnique(final boolean unique)
  {
    _unique = unique;
    return this;
  }

  public IndexMetaDataBuilder addColumn(final ColumnMetaDataBuilder columnMetaDataBuilder)
  {
    _columns.add(columnMetaDataBuilder);
    return this;
  }
}
