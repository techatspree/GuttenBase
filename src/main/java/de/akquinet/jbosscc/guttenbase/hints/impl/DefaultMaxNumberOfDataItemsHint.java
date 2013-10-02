package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.MaxNumberOfDataItemsHint;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.tools.MaxNumberOfDataItems;

/**
 * Default maximum is 30000.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultMaxNumberOfDataItemsHint extends MaxNumberOfDataItemsHint {
	@Override
	public MaxNumberOfDataItems getValue() {
		return new MaxNumberOfDataItems() {

			@Override
			public int getMaxNumberOfDataItems(TableMetaData targetTableMetaData) {
				return 30000;
			}
		};
	}
}
