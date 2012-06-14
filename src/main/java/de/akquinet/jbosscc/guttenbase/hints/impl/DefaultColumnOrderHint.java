package de.akquinet.jbosscc.guttenbase.hints.impl;

import java.util.Comparator;

import de.akquinet.jbosscc.guttenbase.hints.ColumnOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnOrderComparatorFactory;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

/**
 * By default order by natural order of column names.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultColumnOrderHint extends ColumnOrderHint {
	@Override
	public ColumnOrderComparatorFactory getValue() {
		return new ColumnOrderComparatorFactory() {
			@Override
			public Comparator<ColumnMetaData> createComparator() {
				return new Comparator<ColumnMetaData>() {
					@Override
					public int compare(final ColumnMetaData c1, final ColumnMetaData c2) {
						return c1.compareTo(c2);
					}
				};
			}
		};
	}
}
