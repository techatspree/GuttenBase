package de.akquinet.jbosscc.guttenbase.tools;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

public class ReadTableDataToolTest extends AbstractGuttenBaseTest {
	private static final String CONNECTOR_ID1 = "hsqldb";

	private final ReadTableDataTool _objectUnderTest = new ReadTableDataTool(_connectorRepository);

	@Before
	public void setup() throws SQLException {
		_connectorRepository.addConnectionInfo(CONNECTOR_ID1, new TestHsqlConnectionInfo());
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, false, false, "/data/test-data.sql");
	}

	@Test
	public void testReadData() throws Exception {
		final TableMetaData tableMetaData = _connectorRepository.getDatabaseMetaData(CONNECTOR_ID1).getTableMetaData("FOO_USER");

		final List<Map<String, Object>> tableData = _objectUnderTest.readTableData(CONNECTOR_ID1, tableMetaData, -1);
		assertEquals(5, tableData.size());

		final Map<String, Object> rowData = tableData.get(0);
		assertEquals(6, rowData.size());
		assertEquals(123, rowData.get("PERSONAL_NUMBER"));
		assertEquals(1L, rowData.get("ID"));
	}

	@Test
	public void testReadData1Line() throws Exception {
		final TableMetaData tableMetaData = _connectorRepository.getDatabaseMetaData(CONNECTOR_ID1).getTableMetaData("FOO_USER");

		final List<Map<String, Object>> tableData = _objectUnderTest.readTableData(CONNECTOR_ID1, tableMetaData, 1);
		assertEquals(1, tableData.size());
	}
}
