package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.TestH2DataSourceConnectionInfo;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DatabaseSourceConnectorTest extends AbstractGuttenBaseTest {
  private static final String CONNECTOR_ID = "h2";

  @Before
  public void setup() throws Exception {
    _connectorRepository.addConnectionInfo(CONNECTOR_ID, new TestH2DataSourceConnectionInfo());

    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID, "/ddl/tables.sql");
  }

  @Test
  public void testMetaData() throws Exception {
    final DatabaseMetaData databaseMetaData = _connectorRepository.getDatabaseMetaData(CONNECTOR_ID);
    assertNotNull(databaseMetaData);
    assertEquals("H2", databaseMetaData.getDatabaseMetaData().getDatabaseProductName());
  }
}
