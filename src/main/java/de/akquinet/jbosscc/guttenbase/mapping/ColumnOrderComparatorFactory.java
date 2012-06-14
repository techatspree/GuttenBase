package de.akquinet.jbosscc.guttenbase.mapping;

import java.util.Comparator;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

/**
 * Determine order of columns in SELECT/INSERT statement.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 */
public interface ColumnOrderComparatorFactory {
	Comparator<ColumnMetaData> createComparator();
}
