package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;
import de.akquinet.jbosscc.guttenbase.tools.NumberOfCheckedTableData;

/**
 * How many rows of the copied tables shall be regarded when checking that data has been transferred correctly with the
 * {@link CheckEqualTableDataTool} tool.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 *
 * Hint is used by {@link CheckEqualTableDataTool} How many rows of tables shall be regarded when checking that data has been transferred correctly.
 * @author M. Dahm
 */
public abstract class NumberOfCheckedTableDataHint implements ConnectorHint<NumberOfCheckedTableData> {
	@Override
	public final Class<NumberOfCheckedTableData> getConnectorHintType() {
		return NumberOfCheckedTableData.class;
	}
}
