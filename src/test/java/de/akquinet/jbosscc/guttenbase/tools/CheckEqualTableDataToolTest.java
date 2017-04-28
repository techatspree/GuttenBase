package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.TestH2ConnectionInfo;
import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.exceptions.UnequalDataException;
import de.akquinet.jbosscc.guttenbase.exceptions.UnequalNumberOfRowsException;
import org.junit.Before;
import org.junit.Test;

public class CheckEqualTableDataToolTest extends AbstractGuttenBaseTest {
	private static final String CONNECTOR_ID1 = "hsqldb";
	private static final String CONNECTOR_ID2 = "h2";

	private final CheckEqualTableDataTool _objectUnderTest = new CheckEqualTableDataTool(_connectorRepository);

	@Before
	public void setup() {
		_connectorRepository.addConnectionInfo(CONNECTOR_ID1, new TestHsqlConnectionInfo());
		_connectorRepository.addConnectionInfo(CONNECTOR_ID2, new TestH2ConnectionInfo());
	}

	@Test
	public void testEqualData() throws Exception {
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, false, false, "/data/test-data.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, false, false, "/data/test-data.sql");

		_objectUnderTest.checkTableData(CONNECTOR_ID1, CONNECTOR_ID2);
	}

	@Test(expected = UnequalNumberOfRowsException.class)
	public void testTooFewRows() throws Exception {
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, false, false, "/data/test-data.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, false, false, "/data/test-data-missingRows.sql");

		_objectUnderTest.checkTableData(CONNECTOR_ID1, CONNECTOR_ID2);
	}

	@Test(expected = UnequalDataException.class)
	public void testDifferentData() throws Exception {
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, false, false, "/data/test-data.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, false, false, "/data/test-data-wrongData.sql");

		_objectUnderTest.checkTableData(CONNECTOR_ID1, CONNECTOR_ID2);
	}
}
