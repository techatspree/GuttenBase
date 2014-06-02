package de.akquinet.jbosscc.guttenbase.tools;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;

public class DerbyDropTablesToolTest extends AbstractGuttenBaseTest
{
  private static final String CONNECTOR_ID = "derby";

  private final DropTablesTool _objectUnderTest = new DropTablesTool(_connectorRepository);

  @Before
  public void setup() throws Exception
  {
    _connectorRepository.addConnectionInfo(CONNECTOR_ID, new TestDerbyConnectionInfo());
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID, "/ddl/ubi.sql");
  }

  @Test
  public void testDropTables() throws Exception
  {
    assertFalse(_connectorRepository.getDatabaseMetaData(CONNECTOR_ID).getTableMetaData().isEmpty());
    _objectUnderTest.dropTables(CONNECTOR_ID);
    assertTrue(_connectorRepository.getDatabaseMetaData(CONNECTOR_ID).getTableMetaData().isEmpty());
  }
}
