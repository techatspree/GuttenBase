package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ReadTableDataToolTest extends AbstractGuttenBaseTest {
  private static final String CONNECTOR_ID1 = "hsqldb";
  private ReadTableDataTool _objectUnderTest;

  @Before
  public void setup() throws SQLException {
    _connectorRepository.addConnectionInfo(CONNECTOR_ID1, new TestHsqlConnectionInfo());
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, false, false, "/data/test-data.sql");

    final TableMetaData tableMetaData = _connectorRepository.getDatabaseMetaData(CONNECTOR_ID1).getTableMetaData("FOO_USER");
    _objectUnderTest = new ReadTableDataTool(_connectorRepository, CONNECTOR_ID1, tableMetaData);
    _objectUnderTest.start();
  }

  @After
  public void end() throws SQLException {
    _objectUnderTest.end();
  }

  @Test
  public void testReadAllData() throws Exception {
    final List<Map<String, Object>> tableData = _objectUnderTest.readTableData(-1);
    assertEquals(5, tableData.size());

    final Map<String, Object> rowData = tableData.get(0);
    assertEquals(6, rowData.size());
    assertEquals((short) 123, rowData.get("PERSONAL_NUMBER"));
    assertEquals(1L, rowData.get("ID"));
  }

  @Test
  public void testReadData1Line() throws Exception {
    final List<Map<String, Object>> tableData = _objectUnderTest.readTableData(1);
    assertEquals(1, tableData.size());
  }

  @Test
  public void testReadDataSubsequently() throws Exception {
    int lines = 0;

    while (_objectUnderTest.readTableData(1) != null) {
      lines++;
    }

    assertEquals(5, lines);
  }
}
