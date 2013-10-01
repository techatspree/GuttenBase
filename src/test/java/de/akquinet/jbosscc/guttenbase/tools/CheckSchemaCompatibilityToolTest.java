package de.akquinet.jbosscc.guttenbase.tools;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;
import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.exceptions.IncompatibleColumnsException;
import de.akquinet.jbosscc.guttenbase.exceptions.IncompatibleTablesException;

public class CheckSchemaCompatibilityToolTest extends AbstractGuttenBaseTest
{
  private static final String CONNECTOR_ID1 = "hsqldb";
  private static final String CONNECTOR_ID2 = "derby";

  private final CheckSchemaCompatibilityTool _objectUnderTest = new CheckSchemaCompatibilityTool(_connectorRepository);

  @Before
  public void setup()
  {
    _connectorRepository.addConnectionInfo(CONNECTOR_ID1, new TestHsqlConnectionInfo());
    _connectorRepository.addConnectionInfo(CONNECTOR_ID2, new TestDerbyConnectionInfo());
  }

  @Test(expected = IncompatibleTablesException.class)
  public void testNoTables() throws Exception
  {
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");

    _objectUnderTest.checkTableConfiguration(CONNECTOR_ID1, CONNECTOR_ID2);
  }

  @Test(expected = IncompatibleTablesException.class)
  public void testMissingTable() throws Exception
  {
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, "/ddl/tables-missingTable.sql");

    _objectUnderTest.checkTableConfiguration(CONNECTOR_ID1, CONNECTOR_ID2);
  }

  @Test(expected = IncompatibleColumnsException.class)
  public void testMissingColumn() throws Exception
  {
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, "/ddl/tables-incompatibleColumn.sql");

    _objectUnderTest.checkTableConfiguration(CONNECTOR_ID1, CONNECTOR_ID2);
  }
}
