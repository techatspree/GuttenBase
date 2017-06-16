package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;
import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultColumnDataMapperProvider;
import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultColumnMapper;
import de.akquinet.jbosscc.guttenbase.hints.ColumnDataMapperProviderHint;
import de.akquinet.jbosscc.guttenbase.hints.ColumnMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultColumnDataMapperProviderHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnDataMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Split column data into multiple columns in target database.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class SplitColumnDataTest extends AbstractGuttenBaseTest {
  public static final String CONNECTOR_ID1 = "derby";
  public static final String CONNECTOR_ID2 = "h2";

  @Before
  public final void setup() throws Exception {
    _connectorRepository.addConnectionInfo(CONNECTOR_ID1,
            new TestHsqlConnectionInfo());
    _connectorRepository.addConnectionInfo(CONNECTOR_ID2,
            new TestDerbyConnectionInfo());

    new ScriptExecutorTool(_connectorRepository)
            .executeScript(CONNECTOR_ID1,
                    "CREATE TABLE FOO(ID bigint PRIMARY KEY, STUPID varchar(255));");
    new ScriptExecutorTool(_connectorRepository)
            .executeScript(CONNECTOR_ID2,
                    "CREATE TABLE FOO(ID bigint PRIMARY KEY, SMART1 varchar(100), SMART2 varchar(100));");

    new ScriptExecutorTool(_connectorRepository).executeScript(
            CONNECTOR_ID1, false, false,
            "INSERT INTO FOO(ID, STUPID) VALUES(1, 'A|B');");
  }

  @Test
  public void testSplitColumn() throws Exception {
    ColumnMapperHint columnMapperHint = new ColumnMapperHint() {
      @Override
      public ColumnMapper getValue() {
        return new DefaultColumnMapper() {
          @Override
          public ColumnMapperResult map(ColumnMetaData source,
                                        TableMetaData targetTableMetaData)
                  throws SQLException {
            if (source.getColumnName().equalsIgnoreCase("STUPID")) {
              return new ColumnMapperResult(Arrays.asList(targetTableMetaData.getColumnMetaData("SMART1"), targetTableMetaData.getColumnMetaData("SMART2")));
            }
            else {
              return super.map(source, targetTableMetaData);
            }
          }
        };
      }
    };

    ColumnDataMapperProviderHint columnDataMapperProviderHint = new DefaultColumnDataMapperProviderHint() {
      @Override
      protected void addMappings(DefaultColumnDataMapperProvider columnDataMapperFactory) {
        super.addMappings(columnDataMapperFactory);
        columnDataMapperFactory.addMapping(ColumnType.CLASS_STRING, ColumnType.CLASS_STRING, new ColumnDataMapper() {
          @Override
          public boolean isApplicable(ColumnMetaData sourceColumnMetaData, ColumnMetaData targetColumnMetaData) throws SQLException {
            return true;
          }

          @Override
          public Object map(ColumnMetaData sourceColumnMetaData, ColumnMetaData targetColumnMetaData, Object value) throws SQLException {
            if (value != null) {
              final String columnName = targetColumnMetaData.getColumnName();
              final String text = value.toString();
              final String[] strings = text.split("\\|");

              if ("SMART1".equalsIgnoreCase(columnName)) {
                return strings[0];
              }
              else {
                return strings[1];
              }

            }
            else {
              return null;
            }
          }
        });
      }
    };

    _connectorRepository.addConnectorHint(CONNECTOR_ID2, columnMapperHint);
    _connectorRepository.addConnectorHint(CONNECTOR_ID2, columnDataMapperProviderHint);

    new DefaultTableCopyTool(_connectorRepository).copyTables(CONNECTOR_ID1, CONNECTOR_ID2);

    final TableMetaData tableMetaData = _connectorRepository
            .getDatabaseMetaData(CONNECTOR_ID2).getTableMetaData("FOO");

    assertEquals(1, tableMetaData.getTotalRowCount());

    final List<Map<String, Object>> tableData = new ReadTableDataTool(
            _connectorRepository).readTableData(CONNECTOR_ID2,
            tableMetaData, 1);
    final Map<String, Object> row = tableData.get(0);

    assertEquals(1L, row.get("ID"));
    assertEquals("A", row.get("SMART1"));
    assertEquals("B", row.get("SMART2"));
  }
}
