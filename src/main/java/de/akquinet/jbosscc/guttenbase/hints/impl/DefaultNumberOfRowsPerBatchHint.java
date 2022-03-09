package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.NumberOfRowsPerBatchHint;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.tools.NumberOfRowsPerBatch;

/**
 * Default number of VALUES clauses is 2000.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DefaultNumberOfRowsPerBatchHint extends NumberOfRowsPerBatchHint {
	@Override
	public NumberOfRowsPerBatch getValue() {
		return new NumberOfRowsPerBatch() {
			@Override
			public int getNumberOfRowsPerBatch(TableMetaData targetTableMetaData) {
				return 2000;
			}

			@Override
			public boolean useMultipleValuesClauses(TableMetaData targetTableMetaData) {
				return true;
			}
		};
	}
}
