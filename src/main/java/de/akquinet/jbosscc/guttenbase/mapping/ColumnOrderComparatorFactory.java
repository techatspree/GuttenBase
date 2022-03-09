package de.akquinet.jbosscc.guttenbase.mapping;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

import java.util.Comparator;

/**
 * Determine order of columns in SELECT/INSERT statement.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 */
public interface ColumnOrderComparatorFactory {
	Comparator<ColumnMetaData> createComparator();
}
