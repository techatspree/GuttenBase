package de.akquinet.jbosscc.guttenbase.meta.impl;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Information about a foreign key between table columns.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ForeignKeyMetaDataImpl implements InternalForeignKeyMetaData {
  private static final long serialVersionUID = 1L;

  private final String _foreignKeyName;
  private final ColumnMetaData _referencingColumn;
  private final ColumnMetaData _referencedColumn;
  private final TableMetaData _tableMetaData;

  public ForeignKeyMetaDataImpl(final TableMetaData tableMetaData, final String foreignKeyName, final ColumnMetaData referencingColumn,
                                final ColumnMetaData referencedColumn) {
    assert tableMetaData != null : "tableMetaData != null";
    assert foreignKeyName != null : "foreignKeyName != null";
    assert referencingColumn != null : "referencingColumn != null";
    assert referencedColumn != null : "referencedColumn != null";

    _tableMetaData = tableMetaData;
    _foreignKeyName = foreignKeyName;
    _referencingColumn = referencingColumn;
    _referencedColumn = referencedColumn;
  }

  @Override
  public TableMetaData getTableMetaData() {
    return _tableMetaData;
  }

  @Override
  public String getForeignKeyName() {
    return _foreignKeyName;
  }

  @Override
  public ColumnMetaData getReferencingColumn() {
    return _referencingColumn;
  }

  @Override
  public ColumnMetaData getReferencedColumn() {
    return _referencedColumn;
  }

  @Override
  public int compareTo(final ForeignKeyMetaData that) {
    return this.getForeignKeyName().toUpperCase().compareTo(that.getForeignKeyName().toUpperCase());
  }

  @Override
  public String toString() {
    return getTableMetaData() + ":" + getForeignKeyName() + ":" + getReferencingColumn() + "->" + getReferencedColumn();
  }

  @Override
  public int hashCode() {
    return getForeignKeyName().toUpperCase().hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    final ForeignKeyMetaData that = (ForeignKeyMetaData) obj;

    return this.getForeignKeyName().equalsIgnoreCase(that.getForeignKeyName());
  }
}
