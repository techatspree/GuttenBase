package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.NumberOfCheckedTableDataHint;
import de.akquinet.jbosscc.guttenbase.tools.NumberOfCheckedTableData;

/**
 * Default number of checked rows is 100.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultNumberOfCheckedTableDataHint extends NumberOfCheckedTableDataHint {
	@Override
	public NumberOfCheckedTableData getValue() {
		return () -> 100;
	}
}
