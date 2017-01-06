package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.tools.NumberOfCheckedTableData;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;

/**
 * How many rows of the copied tables shall be regarded when checking that data has been transferred correctly with the
 * {@link CheckEqualTableDataTool} tool.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @gb.ApplicableForTarget
 * @gb.HintUsedBy {@link CheckEqualTableDataTool} How many rows of tables shall be regarded when checking that data has been transferred correctly.
 * @author M. Dahm
 */
public abstract class NumberOfCheckedTableDataHint implements ConnectorHint<NumberOfCheckedTableData> {
	@Override
	public final Class<NumberOfCheckedTableData> getConnectorHintType() {
		return NumberOfCheckedTableData.class;
	}
}
