package de.akquinet.jbosscc.guttenbase.meta;

/**
 * Extension for internal access.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public interface InternalTableMetaData extends TableMetaData
{
  void setTotalRowCount(final int rowCount);

  void setFilteredRowCount(final int rowCount);

  void addColumn(final ColumnMetaData column);

  void removeColumn(ColumnMetaData columnMetaData);

  void addIndex(IndexMetaData indexMetaData);

  void addImportedForeignKey(ForeignKeyMetaData fkMetaData);

  void addExportedForeignKey(ForeignKeyMetaData fkMetaData);
}
