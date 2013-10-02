package de.akquinet.jbosscc.guttenbase.meta;

import java.util.UUID;

/**
 * Extension for internal access.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface InternalColumnMetaData extends ColumnMetaData {
	/**
	 * Make columns globally uniq since the name may not be uniq within the data base.
	 */
	UUID getColumnId();

	void setPrimaryKey(final boolean primaryKey);
}