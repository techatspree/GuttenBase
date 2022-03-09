package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultSplitColumn;
import de.akquinet.jbosscc.guttenbase.hints.SplitColumnHint;
import de.akquinet.jbosscc.guttenbase.tools.SplitColumn;

/**
 * Sometimes the amount of data exceeds buffers. In these cases we need to split the data by some given range, usually the primary key.
 * I.e., the data is read in chunks where these chunks are split using the ID column range of values.
 * <p>
 * By default use the first primary key column, if any. Otherwise returns the first column.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DefaultSplitColumnHint extends SplitColumnHint {
	@Override
	public SplitColumn getValue() {
		return new DefaultSplitColumn();
	}
}
