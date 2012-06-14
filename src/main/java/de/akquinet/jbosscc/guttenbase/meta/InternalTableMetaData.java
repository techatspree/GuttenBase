package de.akquinet.jbosscc.guttenbase.meta;

/**
 * Extension for internal access.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface InternalTableMetaData extends TableMetaData {
	void setRowCount(final int rowCount);

	void addColumn(final ColumnMetaData column);

	void addIndex(IndexMetaData indexMetaData);

	void addForeignKey(ForeignKeyMetaData fkMetaData);

	void clearColumns();
}