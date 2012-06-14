package de.akquinet.jbosscc.guttenbase.meta.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalTableMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Information about a table.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class TableMetaDataImpl implements InternalTableMetaData {
	private static final long serialVersionUID = 1L;

	private final String _tableName;
	private int _rowCount;
	private final Map<String, ColumnMetaData> _columns = new LinkedHashMap<String, ColumnMetaData>();
	private final Map<String, IndexMetaData> _indexes = new LinkedHashMap<String, IndexMetaData>();
	private final Map<String, ForeignKeyMetaData> _foreignKeys = new LinkedHashMap<String, ForeignKeyMetaData>();
	private final DatabaseMetaData _databaseMetaData;

	public TableMetaDataImpl(final String tableName, final DatabaseMetaData databaseMetaData) {
		assert tableName != null : "tableName != null";
		assert databaseMetaData != null : "databaseMetaData != null";
		_tableName = tableName;
		_databaseMetaData = databaseMetaData;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRowCount() {
		return _rowCount;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRowCount(final int rowCount) {
		_rowCount = rowCount;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ColumnMetaData> getColumnMetaData() {
		return new ArrayList<ColumnMetaData>(_columns.values());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnMetaData getColumnMetaData(final String columnName) {
		return _columns.get(columnName.toUpperCase());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addColumn(final ColumnMetaData columnMetaData) {
		assert columnMetaData != null : "columnMetaData != null";
		_columns.put(columnMetaData.getColumnName().toUpperCase(), columnMetaData);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getColumnCount() {
		return getColumnMetaData().size();
	}

	@Override
	public IndexMetaData getIndexMetaData(final String indexName) {
		return _indexes.get(indexName.toUpperCase());

	}

	@Override
	public List<IndexMetaData> getIndexes() {
		return new ArrayList<IndexMetaData>(_indexes.values());
	}

	@Override
	public List<ForeignKeyMetaData> getForeignKeys() {
		return new ArrayList<ForeignKeyMetaData>(_foreignKeys.values());
	}

	@Override
	public void addIndex(final IndexMetaData indexMetaData) {
		_indexes.put(indexMetaData.getIndexName().toUpperCase(), indexMetaData);
	}

	@Override
	public void addForeignKey(final ForeignKeyMetaData fkMetaData) {
		_foreignKeys.put(fkMetaData.getForeignKeyName().toUpperCase(), fkMetaData);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ColumnMetaData> getPrimaryKeyColumns() {
		final List<ColumnMetaData> result = new ArrayList<ColumnMetaData>();

		for (final ColumnMetaData columnMetaData : getColumnMetaData()) {
			if (columnMetaData.isPrimaryKey()) {
				result.add(columnMetaData);
			}
		}

		return result;
	}

	@Override
	public List<IndexMetaData> getIndexesForColumn(final ColumnMetaData columnMetaData) {
		final List<IndexMetaData> result = new ArrayList<IndexMetaData>();

		for (final IndexMetaData index : getIndexes()) {
			for (final ColumnMetaData column : index.getColumnMetaData()) {
				if (column.equals(columnMetaData)) {
					result.add(index);
				}
			}
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTableName() {
		return _tableName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabaseMetaData getDatabaseMetaData() {
		return _databaseMetaData;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearColumns() {
		_columns.clear();
		_indexes.clear();
		_foreignKeys.clear();
	}

	@Override
	public int compareTo(final TableMetaData that) {
		return this.getTableName().toUpperCase().compareTo(that.getTableName().toUpperCase());
	}

	@Override
	public String toString() {
		return getTableName();
	}

	@Override
	public int hashCode() {
		return getTableName().toUpperCase().hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		final TableMetaData that = (TableMetaData) obj;

		return this.getTableName().equalsIgnoreCase(that.getTableName());
	}
}
