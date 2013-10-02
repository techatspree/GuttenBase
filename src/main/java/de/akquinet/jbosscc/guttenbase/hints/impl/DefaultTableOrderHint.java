package de.akquinet.jbosscc.guttenbase.hints.impl;

import java.util.Comparator;

import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.TableOrderComparatorFactory;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * By default order by natural order of table names.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultTableOrderHint extends TableOrderHint {
	@Override
	public TableOrderComparatorFactory getValue() {
		return new TableOrderComparatorFactory() {
			@Override
			public Comparator<TableMetaData> createComparator() {
				return new Comparator<TableMetaData>() {
					@Override
					public int compare(final TableMetaData t1, final TableMetaData t2) {
						return t1.compareTo(t2);
					}
				};
			}
		};
	}
}
