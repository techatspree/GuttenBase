package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinMaxIdSelectorTest extends AbstractGuttenBaseTest {
	public static final String CONNECTOR_ID = "hsql";

	private final MinMaxIdSelectorTool _objectUnderTest = new MinMaxIdSelectorTool(_connectorRepository);

	@Before
	public void setup() throws Exception {
		_connectorRepository.addConnectionInfo(CONNECTOR_ID, new TestHsqlConnectionInfo());
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID, false, false, "/data/test-data.sql");
	}

	@Test
	public void testMinMax() throws Exception {
		final TableMetaData tableMetaData = _connectorRepository.getDatabaseMetaData(CONNECTOR_ID).getTableMetaData("FOO_COMPANY");

		_objectUnderTest.computeMinMax(CONNECTOR_ID, tableMetaData);

		assertEquals(4, tableMetaData.getTotalRowCount());
		assertEquals(1, _objectUnderTest.getMinValue());
		assertEquals(7, _objectUnderTest.getMaxValue());
	}
}
