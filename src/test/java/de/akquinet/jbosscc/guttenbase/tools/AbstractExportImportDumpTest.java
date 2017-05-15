package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;
import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.export.ExportDumpConnectorInfo;
import de.akquinet.jbosscc.guttenbase.export.ExportDumpExtraInformation;
import de.akquinet.jbosscc.guttenbase.export.ImportDumpConnectionInfo;
import de.akquinet.jbosscc.guttenbase.export.ImportDumpExtraInformation;
import de.akquinet.jbosscc.guttenbase.hints.ExportDumpExtraInformationHint;
import de.akquinet.jbosscc.guttenbase.hints.ImportDumpExtraInformationHint;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("deprecation")
public abstract class AbstractExportImportDumpTest extends AbstractGuttenBaseTest {
  public static final String DATA_JAR = "./data.jar";
  public static final String IMPORT = "import";
  public static final String EXPORT = "export";
  public static final String CONNECTOR_ID1 = "derby";
  public static final String CONNECTOR_ID2 = "h2";

  public static final String KEY = "SEQUENCE_NUMBER";
  public static final long VALUE = 4711L;

  private Map<String, Serializable> _extraInformation;

  @Before
  public final void setupConnectors() throws Exception {
    new File(DATA_JAR).delete();

    _connectorRepository.addConnectionInfo(CONNECTOR_ID1, new TestDerbyConnectionInfo());
    _connectorRepository.addConnectionInfo(CONNECTOR_ID2, new TestHsqlConnectionInfo());
    _connectorRepository.addConnectionInfo(EXPORT, new ExportDumpConnectorInfo(CONNECTOR_ID1, DATA_JAR));
    _connectorRepository.addConnectionInfo(IMPORT, new ImportDumpConnectionInfo(new File(DATA_JAR).toURI().toURL()));

    _connectorRepository.addConnectorHint(EXPORT, new ExportDumpExtraInformationHint() {
      @Override
      public ExportDumpExtraInformation getValue() {
        return (connectorRepository, connectorId, exportDumpConnectionInfo) -> {
          final Map<String, Serializable> result = new HashMap<>();
          result.put(KEY, VALUE);
          return result;
        };
      }
    });

    _connectorRepository.addConnectorHint(IMPORT, new ImportDumpExtraInformationHint() {
      @Override
      public ImportDumpExtraInformation getValue() {
        return extraInformation -> _extraInformation = extraInformation;
      }
    });
  }

  @Test
  public void testExportImportRoundtripWithExtraInformation() throws Exception {
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, "/ddl/tables.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, false, false, "/data/test-data.sql");

    for (int i = 1; i < 5; i++) {
      insertBinaryData(CONNECTOR_ID1, i);
    }

    assertFalse(new SchemaComparatorTool(_connectorRepository).check(CONNECTOR_ID1, EXPORT).isSevere());
    assertFalse(new SchemaComparatorTool(_connectorRepository).check(CONNECTOR_ID1, CONNECTOR_ID2).isSevere());

    new DefaultTableCopyTool(_connectorRepository).copyTables(CONNECTOR_ID1, EXPORT);
    new DefaultTableCopyTool(_connectorRepository).copyTables(IMPORT, CONNECTOR_ID2);

    new CheckEqualTableDataTool(_connectorRepository).checkTableData(CONNECTOR_ID1, CONNECTOR_ID2);

    assertNotNull("Extra info has been set", _extraInformation);
    assertEquals(VALUE, _extraInformation.get(KEY));
    checkDump();
  }

  protected void checkDump() throws Exception {
  }
}
