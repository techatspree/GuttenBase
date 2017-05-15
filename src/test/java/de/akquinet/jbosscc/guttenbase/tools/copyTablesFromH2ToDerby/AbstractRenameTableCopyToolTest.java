package de.akquinet.jbosscc.guttenbase.tools.copyTablesFromH2ToDerby;


import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;
import de.akquinet.jbosscc.guttenbase.configuration.TestH2ConnectionInfo;
import de.akquinet.jbosscc.guttenbase.exceptions.TableConfigurationException;
import de.akquinet.jbosscc.guttenbase.hints.ColumnMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.NumberOfRowsPerBatchHint;
import de.akquinet.jbosscc.guttenbase.hints.RefreshTargetConnectionHint;
import de.akquinet.jbosscc.guttenbase.hints.TableMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.TestColumnRenameNameMapper;
import de.akquinet.jbosscc.guttenbase.hints.TestTableRenameNameMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.tools.AbstractGuttenBaseTest;
import de.akquinet.jbosscc.guttenbase.tools.AbstractTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;
import de.akquinet.jbosscc.guttenbase.tools.NumberOfRowsPerBatch;
import de.akquinet.jbosscc.guttenbase.tools.RefreshTargetConnection;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;

public abstract class AbstractRenameTableCopyToolTest extends AbstractGuttenBaseTest {
  public static final String CONNECTOR_SOURCE = "hsqldb";
  public static final String CONNECTOR_TARGET = "derby";

  @Before
  public void setup() {
    _connectorRepository.addConnectionInfo(CONNECTOR_TARGET, new TestH2ConnectionInfo());
    _connectorRepository.addConnectionInfo(CONNECTOR_SOURCE, new TestDerbyConnectionInfo());


    _connectorRepository.addConnectorHint(CONNECTOR_TARGET, new TableMapperHint() {
      @Override
      public TableMapper getValue() {
        return new TestTableRenameNameMapper()
                .addReplacement("OFFICES", "TAB_OFFICES")
                .addReplacement("ORDERS", "TAB_ORDERS");
      }
    });

    _connectorRepository.addConnectorHint(CONNECTOR_TARGET, new ColumnMapperHint() {
      @Override
      public ColumnMapper getValue() {
        return new TestColumnRenameNameMapper()
                .addReplacement("PHONE", "ID_PHONE")
                .addReplacement("CITY", "ID_CITY");
      }
    });
  }


  @Test(expected = TableConfigurationException.class)
  public void testCopyFailsMissingTables() throws Exception {
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_SOURCE, "/ddl/h2/script-allshop-h2-mod.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_SOURCE, false, false, "/data/allshop-data.sql");

    getCopyTool().copyTables(CONNECTOR_SOURCE, CONNECTOR_TARGET);
  }

  @Test
  public void testCopyWithMultipleValuesClauses() throws Exception {
    addNumberOfRowsPerBatchHint(2, true);
    setupSourceData();
    getCopyTool().copyTables(CONNECTOR_SOURCE, CONNECTOR_TARGET);
    new CheckEqualTableDataTool(_connectorRepository).checkTableData(CONNECTOR_SOURCE, CONNECTOR_TARGET);
  }

  @Test
  public void testCopyWithBatchMode() throws Exception {
    addNumberOfRowsPerBatchHint(2, false);

    setupSourceData();
    getCopyTool().copyTables(CONNECTOR_SOURCE, CONNECTOR_TARGET);
    new CheckEqualTableDataTool(_connectorRepository).checkTableData(CONNECTOR_SOURCE, CONNECTOR_TARGET);
  }

  @Test
  public void testRefreshConnection() throws Exception {
    _connectorRepository.addConnectorHint(CONNECTOR_TARGET, new RefreshTargetConnectionHint() {
      @Override
      public RefreshTargetConnection getValue() {
        return (noCopiedTables, sourceTableMetaData) -> noCopiedTables % 2 == 0;
      }
    });

    setupSourceData();
    getCopyTool().copyTables(CONNECTOR_SOURCE, CONNECTOR_TARGET);
    new CheckEqualTableDataTool(_connectorRepository).checkTableData(CONNECTOR_SOURCE, CONNECTOR_TARGET);
  }

  private void addNumberOfRowsPerBatchHint(final int numberOfRowsPerBatch, final boolean useMultipleValuesClauses) {
    _connectorRepository.addConnectorHint(CONNECTOR_SOURCE, new NumberOfRowsPerBatchHint() {
      @Override
      public NumberOfRowsPerBatch getValue() {
        return new NumberOfRowsPerBatch() {
          @Override
          public int getNumberOfRowsPerBatch(final TableMetaData targetTableMetaData) {
            return numberOfRowsPerBatch;
          }

          @Override
          public boolean useMultipleValuesClauses(final TableMetaData targetTableMetaData) {
            return useMultipleValuesClauses;
          }
        };
      }
    });
  }

  private void setupSourceData() throws SQLException {
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_SOURCE, "/ddl/h2/script-allshop-h2-mod.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_TARGET, "/ddl/derby/schema-with-rename-derby.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_SOURCE, false, false, "/data/allshop-data.sql");

    for (int i = 1; i < 5; i++) {
      insertBinaryData(CONNECTOR_SOURCE, i);
    }
  }

  protected abstract AbstractTableCopyTool getCopyTool();
}

