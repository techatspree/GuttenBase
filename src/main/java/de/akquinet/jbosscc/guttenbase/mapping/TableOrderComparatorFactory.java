package de.akquinet.jbosscc.guttenbase.mapping;

import java.util.Comparator;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Determine order of tables during copying/comparison.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 */
public interface TableOrderComparatorFactory {
	Comparator<TableMetaData> createComparator();
}
