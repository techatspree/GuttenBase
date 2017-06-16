package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.TestH2ConnectionInfo;
import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    _objectUnderTest.dropForeignKeys(CONNECTOR_ID);

    assertEquals(0, getAllForeignKeys().size());
  }

  @Test
  public void testDropIndexes() throws Exception
  {
    _objectUnderTest.dropForeignKeys(CONNECTOR_ID);

    assertTrue(getAllIndexes().size() > 0);

    _objectUnderTest.dropIndexes(CONNECTOR_ID);

    assertEquals(0, getAllIndexes().size());
  }

  @Test
  public void testDropTables() throws Exception
  {
    assertFalse(_connectorRepository.getDatabaseMetaData(CONNECTOR_ID).getTableMetaData().isEmpty());
    _objectUnderTest.dropTables(CONNECTOR_ID);
    assertTrue(_connectorRepository.getDatabaseMetaData(CONNECTOR_ID).getTableMetaData().isEmpty());
  }

  @Test
  public void testClearTables() throws Exception {
    assertEquals(5, _connectorRepository.getDatabaseMetaData(CONNECTOR_ID).getTableMetaData("FOO_USER").getTotalRowCount());
    _objectUnderTest.clearTables(CONNECTOR_ID);
    assertEquals(0, _connectorRepository.getDatabaseMetaData(CONNECTOR_ID).getTableMetaData("FOO_USER").getTotalRowCount());
  }

  private List<ForeignKeyMetaData> getAllForeignKeys() throws SQLException
  {
    final List<TableMetaData> tableMetaData = TableOrderHint.getSortedTables(_connectorRepository, CONNECTOR_ID);
    final List<ForeignKeyMetaData> allKeys = new ArrayList<>();

    for (final TableMetaData table : tableMetaData)
    {
      allKeys.addAll(table.getExportedForeignKeys());
    }

    return allKeys;
  }

  private List<IndexMetaData> getAllIndexes() throws SQLException
  {
    final List<TableMetaData> tableMetaData = TableOrderHint.getSortedTables(_connectorRepository, CONNECTOR_ID);
    final List<IndexMetaData> allIndexes = new ArrayList<>();

    for (final TableMetaData table : tableMetaData)
    {
      final List<IndexMetaData> indexes = table.getIndexes();

      for (final IndexMetaData indexMetaData : indexes)
      {
        if (!indexMetaData.isPrimaryKeyIndex())
        {
          allIndexes.add(indexMetaData);
        }
      }
    }

    return allIndexes;
  }
}
