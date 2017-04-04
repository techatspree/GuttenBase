package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultColumnComparator;
import de.akquinet.jbosscc.guttenbase.hints.ColumnOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnOrderComparatorFactory;

/**
 * By default order by natural order of column names.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultColumnOrderHint extends ColumnOrderHint {
	@Override
	public ColumnOrderComparatorFactory getValue() {
		return DefaultColumnComparator::new;
	}
}
