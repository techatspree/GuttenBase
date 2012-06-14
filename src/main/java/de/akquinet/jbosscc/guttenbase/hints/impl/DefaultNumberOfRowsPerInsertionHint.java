package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.NumberOfRowsPerInsertionHint;
import de.akquinet.jbosscc.guttenbase.tools.NumberOfRowsPerInsertion;

/**
 * Default number of VALUES clauses is 2000.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultNumberOfRowsPerInsertionHint extends NumberOfRowsPerInsertionHint {
	@Override
	public NumberOfRowsPerInsertion getValue() {
		return new NumberOfRowsPerInsertion() {
			@Override
			public int getNumberOfRowsPerInsertion() {
				return 2000;
			}

			@Override
			public boolean useValuesClauses() {
				return true;
			}
		};
	}
}
