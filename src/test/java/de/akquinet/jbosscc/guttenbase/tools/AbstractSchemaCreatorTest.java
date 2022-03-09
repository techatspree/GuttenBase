package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.connector.ConnectorInfo;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class AbstractSchemaCreatorTest extends AbstractGuttenBaseTest {
  private final ScriptExecutorTool _objectUnderTest = new ScriptExecutorTool(_connectorRepository);
  private final String _connectorId;

  public AbstractSchemaCreatorTest(final String connectorId, final ConnectorInfo connectionInfo) {
    _connectorId = connectorId;

    _connectorRepository.addConnectionInfo(_connectorId, connectionInfo);
  }

  @Test
  public void testConfiguration() throws Exception {
    assertNotNull(_connectorRepository.getConnectionInfo(_connectorId));
    assertNotNull(_connectorRepository.getSourceDatabaseConfiguration(_connectorId));
    assertNotNull(_connectorRepository.getTargetDatabaseConfiguration(_connectorId));
  }

  @Test
  public void testCreateSchema() throws Exception {
    _objectUnderTest.executeFileScript(_connectorId, "/ddl/tables.sql");

    final DatabaseMetaData databaseMetaData = _connectorRepository.getDatabaseMetaData(_connectorId);

    assertEquals(6, databaseMetaData.getTableMetaData().size());
    assertEquals(6, databaseMetaData.getTableMetaData("FOO_USER").getColumnMetaData().size());
    assertEquals(6, databaseMetaData.getTableMetaData("FOO_USER").getColumnCount());
  }
}
