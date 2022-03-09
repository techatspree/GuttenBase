package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;
import de.akquinet.jbosscc.guttenbase.tools.AbstractGuttenBaseTest;
import de.akquinet.jbosscc.guttenbase.tools.EntityTableChecker;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EntityTableCheckerHintTest extends AbstractGuttenBaseTest {
  public static final String CONNECTOR_ID = "derby";

  @Before
  public void setup() throws Exception {
    _connectorRepository.addConnectionInfo(CONNECTOR_ID, new TestDerbyConnectionInfo());
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID, "/ddl/tables.sql");
  }

  @Test
  public void testMainTable() throws Exception {
    final EntityTableChecker objectUnderTest = _connectorRepository.getConnectorHint(CONNECTOR_ID, EntityTableChecker.class).getValue();
    assertTrue(objectUnderTest.isEntityTable(_connectorRepository.getDatabaseMetaData(CONNECTOR_ID).getTableMetaData("FOO_COMPANY")));
    assertTrue(objectUnderTest.isEntityTable(_connectorRepository.getDatabaseMetaData(CONNECTOR_ID).getTableMetaData("FOO_USER")));
    assertFalse(objectUnderTest.isEntityTable(_connectorRepository.getDatabaseMetaData(CONNECTOR_ID).getTableMetaData("FOO_USER_COMPANY")));
  }
}
