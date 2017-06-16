package de.akquinet.jbosscc.guttenbase.meta.impl;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalTableMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Information about a table.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class TableMetaDataImpl implements InternalTableMetaData
{
  private static final long serialVersionUID = 1L;

  private final String _tableName;
  private final String _tableType;
  private int _totalRowCount;
  private int _filteredRowCount;
  private final Map<String, ColumnMetaData> _columns = new LinkedHashMap<>();
  private final Map<String, IndexMetaData> _indexes = new LinkedHashMap<>();
  private final Map<String, ForeignKeyMetaData> _importedForeignKeys = new LinkedHashMap<>();
  private final Map<String, ForeignKeyMetaData> _exportedForeignKeys = new LinkedHashMap<>();
  private final DatabaseMetaData _databaseMetaData;

  public TableMetaDataImpl(final String tableName, final DatabaseMetaData databaseMetaData, final String tableType)
  {
    assert tableName != null : "tableName != null";
    assert databaseMetaData != null : "databaseMetaData != null";
    assert tableType != null : "tableType != null";

    _tableName = tableName;
    _tableType = tableType;
    _databaseMetaData = databaseMetaData;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFilteredRowCount() {
    return _filteredRowCount;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFilteredRowCount(final int filteredRowCount) {
    _filteredRowCount = filteredRowCount;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getTotalRowCount()
  {
    return _totalRowCount;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTotalRowCount(final int rowCount)
  {
    _totalRowCount = rowCount;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ColumnMetaData> getColumnMetaData()
  {
    return new ArrayList<>(_columns.values());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ColumnMetaData getColumnMetaData(final String columnName)
  {
    return _columns.get(columnName.toUpperCase());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addColumn(final ColumnMetaData columnMetaData)
  {
    assert columnMetaData != null : "columnMetaData != null";
    _columns.put(columnMetaData.getColumnName().toUpperCase(), columnMetaData);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeColumn(final ColumnMetaData columnMetaData)
  {
    assert columnMetaData != null : "columnMetaData != null";
    _columns.remove(columnMetaData.getColumnName().toUpperCase());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getColumnCount()
  {
    return getColumnMetaData().size();
  }

  @Override
  public IndexMetaData getIndexMetaData(final String indexName)
  {
    return _indexes.get(indexName.toUpperCase());

  }

  @Override
  public List<IndexMetaData> getIndexes()
  {
    return new ArrayList<>(_indexes.values());
  }

  @Override
  public void addIndex(final IndexMetaData indexMetaData)
  {
    _indexes.put(indexMetaData.getIndexName().toUpperCase(), indexMetaData);
  }

  @Override
  public List<ForeignKeyMetaData> getExportedForeignKeys()
  {
    return new ArrayList<>(_exportedForeignKeys.values());
  }

  @Override
  public void addExportedForeignKey(final ForeignKeyMetaData fkMetaData)
  {
    _exportedForeignKeys.put(fkMetaData.getForeignKeyName().toUpperCase(), fkMetaData);
  }

  @Override
  public List<ForeignKeyMetaData> getImportedForeignKeys()
  {
    return new ArrayList<>(_importedForeignKeys.values());
  }

  @Override
  public void addImportedForeignKey(final ForeignKeyMetaData fkMetaData)
  {
    _importedForeignKeys.put(fkMetaData.getForeignKeyName().toUpperCase(), fkMetaData);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ColumnMetaData> getPrimaryKeyColumns()
  {

    return getColumnMetaData().stream().filter(ColumnMetaData::isPrimaryKey).collect(Collectors.toList());
  }

  @Override
  public List<IndexMetaData> getIndexesContainingColumn(final ColumnMetaData columnMetaData)
  {
    final List<IndexMetaData> result = new ArrayList<>();

    for (final IndexMetaData index : getIndexes())
    {
      result.addAll(index.getColumnMetaData().stream().filter(column -> column.equals(columnMetaData))
        .map(column -> index).collect(Collectors.toList()));
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTableName()
  {
    return _tableName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTableType() {
    return _tableType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DatabaseMetaData getDatabaseMetaData()
  {
    return _databaseMetaData;
  }

  @Override
  public int compareTo(final TableMetaData that)
  {
    return this.getTableName().toUpperCase().compareTo(that.getTableName().toUpperCase());
  }

  @Override
  public String toString()
  {
    return getTableName();
  }

  @Override
  public int hashCode()
  {
    return getTableName().toUpperCase().hashCode();
  }

  @Override
  public boolean equals(final Object obj)
  {
    final TableMetaData that = (TableMetaData) obj;

    return this.getTableName().equalsIgnoreCase(that.getTableName());
  }
}
