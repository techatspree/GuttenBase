package de.akquinet.jbosscc.guttenbase.meta;

import java.io.Serializable;

/**
 * Information about a foreign key between table columns.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface ForeignKeyMetaData extends Comparable<ForeignKeyMetaData>, Serializable {
	String getForeignKeyName();

	ColumnMetaData getReferencingColumn();

	ColumnMetaData getReferencedColumn();

	TableMetaData getTableMetaData();
}
