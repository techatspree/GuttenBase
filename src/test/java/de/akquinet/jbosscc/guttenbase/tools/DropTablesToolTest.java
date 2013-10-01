package de.akquinet.jbosscc.guttenbase.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.configuration.TestH2ConnectionInfo;
import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

public class DropTablesToolTest extends AbstractGuttenBaseTest
{
  private static final String CONNECTOR_ID = "hsqldb";

  private final DropTablesTool _objectUnderTest = new DropTablesTool(_connectorRepository);

  @Before
  public void setup() throws Exception
  {
    _connectorRepository.addConnectionInfo(CONNECTOR_ID, new TestH2ConnectionInfo());
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID, "/ddl/tables.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID, false, false, "/data/test-data.sql");
  }

  @Test
  public void testDropForeignKeys() throws Exception
  {
    assertTrue(getAllForeignKeys().size() > 0);

    _objectUnderTest.dropForeignKeys(CONNECTOR_ID, true);

    assertEquals(0, getAllForeignKeys().size());
  }

  private List<ForeignKeyMetaData> getAllForeignKeys() throws SQLException
  {
    final List<TableMetaData> tableMetaData = TableOrderHint.getSortedTables(_connectorRepository, CONNECTOR_ID);
    final List<ForeignKeyMetaData> allKeys = new ArrayList<ForeignKeyMetaData>();

    for (final TableMetaData table : tableMetaData)
    {
      allKeys.addAll(table.getExportedForeignKeys());
    }

    return allKeys;
  }
}
