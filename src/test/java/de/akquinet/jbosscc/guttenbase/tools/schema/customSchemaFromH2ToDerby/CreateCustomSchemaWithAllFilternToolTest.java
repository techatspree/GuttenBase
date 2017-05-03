package de.akquinet.jbosscc.guttenbase.tools.schema.customSchemaFromH2ToDerby;


import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;
import de.akquinet.jbosscc.guttenbase.configuration.TestH2ConnectionInfo;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.hints.*;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.CustomColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.CustomDefaultColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.tools.AbstractGuttenBaseTest;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.CreateCustomSchemaTool;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateCustomSchemaWithAllFilternToolTest extends AbstractGuttenBaseTest {
    private static final String SOURCE_CONNECTOR_ID = "hsqldb";
    private static final String TARGET_CONNECTOR_ID = "derby";

    private final CreateCustomSchemaTool _objectUnderTest = new CreateCustomSchemaTool(_connectorRepository);

    @Before
    public void setup() throws Exception {
        _connectorRepository.addConnectionInfo(SOURCE_CONNECTOR_ID, new TestH2ConnectionInfo());
        _connectorRepository.addConnectionInfo(TARGET_CONNECTOR_ID, new TestDerbyConnectionInfo());

        new ScriptExecutorTool(_connectorRepository).executeFileScript(SOURCE_CONNECTOR_ID, "/ddl/h2/script-allshop-h2-raw.sql");

        _connectorRepository.addConnectorHint(TARGET_CONNECTOR_ID, new TableMapperHint() {
            @Override
            public TableMapper getValue() {
                return new CustomTableRenameNameTest()
                        .addReplacement("OFFICES", "TAB_OFFICES")
                        .addReplacement("ORDERS", "TAB_ORDERS");
            }
        });

        _connectorRepository.addConnectorHint(TARGET_CONNECTOR_ID, new ColumnMapperHint() {
            @Override
            public ColumnMapper getValue() {
                return new CustomColumnRenameNameTest()
                        .addReplacement("OFFICECODE", "ID_OFFICECODE")
                        .addReplacement("ORDERNUMBER", "ID_ORDERNUMBER")
                        .addReplacement("PHONE", "ID_PHONE")
                        .addReplacement("CITY", "ID_CITY");
            }
        });

        //ConnectionHint for Mapping ColumnType
        _connectorRepository.addConnectorHint(SOURCE_CONNECTOR_ID, new CustomColumnTypeMapperHint() {
            @Override
            public CustomColumnTypeMapper getValue() {
                return new CustomDefaultColumnTypeMapper(DatabaseType.H2DB, DatabaseType.DERBY);
            }
        });

    }

    @Test
    public void testScript() throws Exception {

        _objectUnderTest.copySchema(SOURCE_CONNECTOR_ID, TARGET_CONNECTOR_ID);

        assertEquals("After", "TAB_OFFICES", _connectorRepository.getDatabaseMetaData(TARGET_CONNECTOR_ID).
                getTableMetaData("TAB_OFFICES").getTableName());

        assertEquals("After", "TAB_ORDERS", _connectorRepository.getDatabaseMetaData(TARGET_CONNECTOR_ID).
                getTableMetaData("TAB_ORDERS").getTableName());

        assertEquals("After", "ID_CITY", _connectorRepository.getDatabaseMetaData(TARGET_CONNECTOR_ID)
                .getTableMetaData("CUSTOMERS").getColumnMetaData("ID_CITY").getColumnName());

        assertEquals("After", "ID_ORDERNUMBER", _connectorRepository.getDatabaseMetaData(TARGET_CONNECTOR_ID)
                .getTableMetaData("ORDERDETAILS").getColumnMetaData("ID_ORDERNUMBER").getColumnName());


        assertEquals("Before", 10, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData().size());
        assertEquals("Before", 13, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData("CUSTOMERS").getColumnCount());
        assertEquals("Before", 8, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData("EMPLOYEES").getColumnCount());

        _connectorRepository.addConnectorHint(SOURCE_CONNECTOR_ID, new CustomTableNameFilterTest());
        _connectorRepository.addConnectorHint(SOURCE_CONNECTOR_ID, new CustomColumnNameFilterTest());
        _connectorRepository.refreshDatabaseMetaData(SOURCE_CONNECTOR_ID);

        assertEquals("After", 9, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData().size());
        assertEquals("After", 10, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData("CUSTOMERS").getColumnCount());
        assertEquals("Before", 5, _connectorRepository.getDatabaseMetaData(SOURCE_CONNECTOR_ID).getTableMetaData("EMPLOYEES").getColumnCount());
    }
}
