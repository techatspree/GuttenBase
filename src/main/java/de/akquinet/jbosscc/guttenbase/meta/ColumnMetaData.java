package de.akquinet.jbosscc.guttenbase.meta;

import java.io.Serializable;
import java.util.List;

/**
 * Information about a table column.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface ColumnMetaData extends Comparable<ColumnMetaData>, Serializable {
	int getColumnType();

	String getColumnName();

	String getColumnTypeName();

	String getColumnClassName();

	/**
	 * @return containing table
	 */
	TableMetaData getTableMetaData();

	boolean isNullable();

	boolean isAutoIncrement();

	int getPrecision();

	int getScale();

	boolean isPrimaryKey();

	/**
	 * @return referenced column (Foreign key constraint) or null
	 */
	ColumnMetaData getReferencedColumn();

	/**
	 * @return list of referencing columns (Foreign key constraint)
	 */
	List<ColumnMetaData> getReferencedByColumn();
}
