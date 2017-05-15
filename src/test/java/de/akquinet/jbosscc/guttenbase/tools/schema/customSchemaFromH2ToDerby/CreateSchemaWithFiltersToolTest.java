package de.akquinet.jbosscc.guttenbase.tools.schema.customSchemaFromH2ToDerby;


import de.akquinet.jbosscc.guttenbase.configuration.TestH2ConnectionInfo;
import de.akquinet.jbosscc.guttenbase.hints.TestColumnNameFilterHint;
import de.akquinet.jbosscc.guttenbase.hints.TestTableNameFilterHint;
import de.akquinet.jbosscc.guttenbase.tools.AbstractGuttenBaseTest;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateSchemaWithFiltersToolTest extends AbstractGuttenBaseTest {
    private static final String SOURCE_CONNECTOR_ID = "hsqldb";

    @Before
    public void setup() throws Exception {
        _connectorRepository.addConnectionInfo(SOURCE_CONNECTOR_ID, new TestH2ConnectionInfo());
        new ScriptExecutorTool(_connectorRepository).executeFileScript(SOURCE_CONNECTOR_ID, "/ddl/h2/script-allshop-h2-raw.sql");
    }

    @Test
    public void testFilter() throws Exception {


       assertEquals("Before", 10, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData().size());
       assertEquals("Before", 13, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData("CUSTOMERS").getColumnCount());
       assertEquals("Before", 8, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData("EMPLOYEES").getColumnCount());

      _connectorRepository.addConnectorHint(SOURCE_CONNECTOR_ID, new TestTableNameFilterHint());
      _connectorRepository.addConnectorHint(SOURCE_CONNECTOR_ID, new TestColumnNameFilterHint());
        _connectorRepository.refreshDatabaseMetaData(SOURCE_CONNECTOR_ID);

       assertEquals("After", 9, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData().size());
       assertEquals("After", 10, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData("CUSTOMERS").getColumnCount());
       assertEquals("Before", 5, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData("EMPLOYEES").getColumnCount());

    }
}
