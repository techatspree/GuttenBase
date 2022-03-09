package de.akquinet.jbosscc.guttenbase.meta.builder;

import de.akquinet.jbosscc.guttenbase.meta.InternalForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.impl.ForeignKeyMetaDataImpl;

/**
 * Builder class for ForeignKeyMetaData.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ForeignKeyMetaDataBuilder {
  private ColumnMetaDataBuilder _referencingColumn;
  private ColumnMetaDataBuilder _referencedColumn;
  private String _foreignKeyName = "fk";
  private final TableMetaDataBuilder _tableMetaDataBuilder;
  private ForeignKeyMetaDataImpl _result;

  public ForeignKeyMetaDataBuilder(final TableMetaDataBuilder tableMetaDataBuilder) {
    _tableMetaDataBuilder = tableMetaDataBuilder;
  }

  public InternalForeignKeyMetaData build() {
    if (_result == null) {
      _result = new ForeignKeyMetaDataImpl(_tableMetaDataBuilder.build(), _foreignKeyName, _referencingColumn.build(),
          _referencedColumn.build());
    }

    return _result;
  }

  public ForeignKeyMetaDataBuilder setReferencingColumn(final ColumnMetaDataBuilder referencingColumn) {
    assert referencingColumn != null : "referencingColumn != null";
    _referencingColumn = referencingColumn;
    return this;
  }

  public ForeignKeyMetaDataBuilder setReferencedColumn(final ColumnMetaDataBuilder referencedColumn) {
    assert referencedColumn != null : "referencedColumn != null";
    _referencedColumn = referencedColumn;
    return this;
  }

  public ForeignKeyMetaDataBuilder setForeignKeyName(final String foreignKeyName) {
    assert foreignKeyName != null : "foreignKeyName != null";
    _foreignKeyName = foreignKeyName;
    return this;
  }
}
