package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;
import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.export.ExportDumpConnectorInfo;
import de.akquinet.jbosscc.guttenbase.export.ImportDumpConnectionInfo;
import de.akquinet.jbosscc.guttenbase.hints.RepositoryColumnFilterHint;
import de.akquinet.jbosscc.guttenbase.hints.RepositoryTableFilterHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultZipExporterClassResourcesHint;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryColumnFilter;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryTableFilter;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Copy limited set of data from dump source, i.e. just one table (FOO_USER) and only three columns: ID, USERNAME, PASSWORD
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ReadFilteredDataFromImportDumpTest extends AbstractGuttenBaseTest {
  public static final String DATA_JAR = "./data.jar";
  public static final String IMPORT = "import";
  public static final String EXPORT = "export";
  public static final String CONNECTOR_ID1 = "derby";
  public static final String CONNECTOR_ID2 = "h2";

  @Before
  public final void setup() throws Exception {
    new File(DATA_JAR).delete();

    _connectorRepository.addConnectionInfo(CONNECTOR_ID1, new TestHsqlConnectionInfo());
    _connectorRepository.addConnectionInfo(CONNECTOR_ID2, new TestDerbyConnectionInfo());
    _connectorRepository.addConnectionInfo(EXPORT, new ExportDumpConnectorInfo(CONNECTOR_ID1, DATA_JAR));
    _connectorRepository.addConnectionInfo(IMPORT, new ImportDumpConnectionInfo(new File(DATA_JAR).toURI().toURL()));
    _connectorRepository.addConnectorHint(EXPORT, new DefaultZipExporterClassResourcesHint());

    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
    new ScriptExecutorTool(_connectorRepository).executeScript(CONNECTOR_ID2,
      "CREATE TABLE FOO_USER(ID bigint PRIMARY KEY, USERNAME varchar(100), NAME varchar(100), PASSWORD varchar(255));");

    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, false, false, "/data/test-data.sql");
    new DefaultTableCopyTool(_connectorRepository).copyTables(CONNECTOR_ID1, EXPORT);
  }

  @Test
  public void testFilteredImport() throws Exception {
    final RepositoryTableFilterHint tableFilterHint = new RepositoryTableFilterHint() {
      @Override
      public RepositoryTableFilter getValue() {
        return table -> table.getTableName().equalsIgnoreCase("FOO_USER");
      }
    };

    final RepositoryColumnFilterHint columnFilterHint = new RepositoryColumnFilterHint() {
      @Override
      public RepositoryColumnFilter getValue() {
        return column -> {
          final String columnName = column.getColumnName();
          return columnName.equalsIgnoreCase("ID") || columnName.equalsIgnoreCase("USERNAME")
            || columnName.equalsIgnoreCase("PASSWORD");
        };
      }
    };

    _connectorRepository.addConnectorHint(IMPORT, tableFilterHint);
    _connectorRepository.addConnectorHint(IMPORT, columnFilterHint);

    new DefaultTableCopyTool(_connectorRepository).copyTables(IMPORT, CONNECTOR_ID2);

    assertEquals(1, _connectorRepository.getDatabaseMetaData(CONNECTOR_ID2).getTableMetaData().size());
    final TableMetaData tableMetaData = _connectorRepository.getDatabaseMetaData(CONNECTOR_ID2).getTableMetaData("FOO_USER");

    assertEquals(5, tableMetaData.getTotalRowCount());

    final ReadTableDataTool tool = new ReadTableDataTool(_connectorRepository, CONNECTOR_ID2, tableMetaData);
    tool.start();
    final List<Map<String, Object>> tableData = tool.readTableData(1);
    final Map<String, Object> row = tableData.get(0);
    tool.end();

    assertEquals(1L, row.get("ID"));
    assertEquals("User_1", row.get("USERNAME"));
    assertTrue(row.containsKey("NAME"));
    assertNull(row.get("NAME"));
    assertTrue(row.containsKey("PASSWORD"));
    assertEquals("secret", row.get("PASSWORD"));
  }
}
