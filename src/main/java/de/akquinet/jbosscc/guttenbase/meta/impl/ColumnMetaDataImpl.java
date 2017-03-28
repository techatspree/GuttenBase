package de.akquinet.jbosscc.guttenbase.meta.impl;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Information about a table column.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ColumnMetaDataImpl implements InternalColumnMetaData {
  private static final long serialVersionUID = 1L;

  private final int _columnType;
  private final String _columnName;
  private final String _columnTypeName;
  private final String _columnClassName;
  private final boolean _isNullable;
  private final boolean _isAutoIncrement;
  private final int _precision;
  private final int _scale;

  private final TableMetaData _tableMetaData;
  private boolean _primaryKey;
  private final UUID _columnId = UUID.randomUUID();

  public ColumnMetaDataImpl(
          final int columnType,
          final String columnName,
          final String columnTypeName,
          final String columnClassName,
          final boolean isNullable,
          final boolean isAutoIncrement,
          final int precision,
          final int scale,
          final TableMetaData tableMetaData) {
    assert columnClassName != null : "columnClassName != null";
    assert columnTypeName != null : "columnTypeName != null";
    assert columnName != null : "columnName != null";
    assert tableMetaData != null : "tableMetaData != null";

    _precision = precision;
    _scale = scale;
    _isAutoIncrement = isAutoIncrement;
    _tableMetaData = tableMetaData;
    _isNullable = isNullable;

    _columnType = columnType;
    _columnName = columnName;
    _columnTypeName = columnTypeName;
    _columnClassName = columnClassName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPrimaryKey(final boolean primaryKey) {
    _primaryKey = primaryKey;
  }

  @Override
  public boolean isPrimaryKey() {
    return _primaryKey;
  }

  @Override
  public int getColumnType() {
    return _columnType;
  }

  @Override
  public String getColumnName() {
    return _columnName;
  }

  @Override
  public String getColumnTypeName() {
    return _columnTypeName;
  }

  @Override
  public String getColumnClassName() {
    return _columnClassName;
  }

  @Override
  public boolean isNullable() {
    return _isNullable;
  }

  @Override
  public boolean isAutoIncrement() {
    return _isAutoIncrement;
  }

  @Override
  public int getPrecision() {
    return _precision;
  }

  @Override
  public int getScale() {
    return _scale;
  }

  @Override
  public ColumnMetaData getReferencedColumn() {
    for (final ForeignKeyMetaData foreignKey : getTableMetaData().getImportedForeignKeys()) {
      if (foreignKey.getReferencingColumn() == this) {
        return foreignKey.getReferencedColumn();
      }
    }

    return null;
  }

  @Override
  public List<ColumnMetaData> getReferencedByColumn() {
    final List<ColumnMetaData> result = new ArrayList<>();

    for (final ForeignKeyMetaData foreignKey : getTableMetaData().getExportedForeignKeys()) {
      if (foreignKey.getReferencedColumn() == this) {
        result.add(foreignKey.getReferencingColumn());
      }
    }

    return result;
  }

  @Override
  public TableMetaData getTableMetaData() {
    return _tableMetaData;
  }

  /**
   * /** {@inheritDoc}
   */
  @Override
  public UUID getColumnId() {
    return _columnId;
  }

  @Override
  public int compareTo(final ColumnMetaData that) {
    return this.getColumnName().toUpperCase().compareTo(that.getColumnName().toUpperCase());
  }

  @Override
  public String toString() {
    return getTableMetaData() + ":" + getColumnName() + ":" + getColumnTypeName();
  }

  @Override
  public int hashCode() {
    return getColumnName().toUpperCase().hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    final ColumnMetaData that = (ColumnMetaData) obj;

    return this.getColumnName().equalsIgnoreCase(that.getColumnName());
  }
}
