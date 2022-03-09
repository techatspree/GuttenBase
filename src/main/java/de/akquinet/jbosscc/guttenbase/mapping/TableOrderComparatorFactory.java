package de.akquinet.jbosscc.guttenbase.mapping;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

import java.util.Comparator;

/**
 * Determine order of tables during copying/comparison.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 */
public interface TableOrderComparatorFactory {
	Comparator<TableMetaData> createComparator();
}
