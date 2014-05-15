package de.akquinet.jbosscc.guttenbase.tools;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;
import de.akquinet.jbosscc.guttenbase.hints.RandomTableOrderHint;

public class TableOrderTool_Test extends AbstractGuttenBaseTest
{
  private static final String DB = "db";
  private final TableOrderTool _objectUnderTest = new TableOrderTool(_connectorRepository);

  @Before
  public void setup() throws SQLException
  {
    _connectorRepository.addConnectionInfo(DB, new TestDerbyConnectionInfo());
    _connectorRepository.addConnectorHint(DB, new RandomTableOrderHint());

    new ScriptExecutorTool(_connectorRepository).executeFileScript(DB, "/ddl/tables.sql");
  }

  @Test
  public void testTopDown() throws Exception
  {
    final String tables = _objectUnderTest.getOrderedTables(DB, true).toString().replace('[', '|').replace(']', '|')
        .replaceAll(", ", "|");

    assertTrue(tables, tables.indexOf("|FOO_COMPANY|") < tables.indexOf("|FOO_USER_COMPANY|"));
    assertTrue(tables, tables.indexOf("|FOO_COMPANY") < tables.indexOf("|FOO_USER|"));
    assertTrue(tables, tables.indexOf("|FOO_USER|") < tables.indexOf("|FOO_USER_COMPANY|"));
    assertTrue(tables, tables.indexOf("|FOO_USER|") < tables.indexOf("|FOO_USER_ROLES|"));
    assertTrue(tables, tables.indexOf("|FOO_ROLE|") < tables.indexOf("|FOO_USER_ROLES|"));
  }

  @Test
  public void testBottomUp() throws Exception
  {
    final String tables = _objectUnderTest.getOrderedTables(DB, false).toString().replace('[', '|').replace(']', '|')
        .replaceAll(", ", "|");

    assertTrue(tables, tables.indexOf("|FOO_USER_COMPANY|") < tables.indexOf("|FOO_COMPANY|"));
    assertTrue(tables, tables.indexOf("|FOO_USER_COMPANY|") < tables.indexOf("|FOO_USER|"));
    assertTrue(tables, tables.indexOf("|FOO_USER_ROLES|") < tables.indexOf("|FOO_USER|"));
    assertTrue(tables, tables.indexOf("|FOO_USER_ROLES|") < tables.indexOf("|FOO_ROLE|"));
    assertTrue(tables, tables.indexOf("|FOO_USER|") < tables.indexOf("|FOO_COMPANY|"));
  }
}
