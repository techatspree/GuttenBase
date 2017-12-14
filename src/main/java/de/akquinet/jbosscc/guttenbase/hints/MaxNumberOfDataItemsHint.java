package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.tools.AbstractTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.MaxNumberOfDataItems;

/**
 * How many data items may an INSERT statement have. I.e., how many '?' place holders does the database support. This hint may in effect
 * limit the number given by {@link NumberOfRowsPerBatchHint}.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 *
 * Hint is used by {@link AbstractTableCopyTool} to determine maximum number of data items in INSERT statement
 * @author M. Dahm
 */
public abstract class MaxNumberOfDataItemsHint implements ConnectorHint<MaxNumberOfDataItems> {
	@Override
	public final Class<MaxNumberOfDataItems> getConnectorHintType() {
		return MaxNumberOfDataItems.class;
	}
}
