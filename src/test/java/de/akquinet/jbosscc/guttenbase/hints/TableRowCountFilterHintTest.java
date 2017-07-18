package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.TableRowCountFilter;
import org.junit.Assert;
import org.junit.Before;
import java.sql.SQLException;

/**
 * Test omitting row count statement
 * <p/>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class TableRowCountFilterHintTest extends AbstractHintTest {
  public TableRowCountFilterHintTest() {
    super("/ddl/tables.sql", "/ddl/tables.sql", "/data/test-data.sql");
  }

  @Before
  public void setup() throws Exception {
    _connectorRepository.addConnectorHint(SOURCE, new TableRowCountFilterHint() {
      @Override
      public TableRowCountFilter getValue() {
        return new TableRowCountFilter() {
          @Override
          public boolean accept(final TableMetaData tableMetaData) throws SQLException {
            return false;
          }

          @Override
          public int defaultRowCount(final TableMetaData tableMetaData) throws SQLException {
            return tableMetaData.getTableName().equalsIgnoreCase("FOO_DATA") ? 0 : 1;
          }
        };
      }
    });
  }

  @Override
  protected void executeChecks() throws Exception {
    final TableMetaData source = _connectorRepository.getDatabaseMetaData(SOURCE).getTableMetaData("FOO_USER");
    final TableMetaData target = _connectorRepository.getDatabaseMetaData(TARGET).getTableMetaData("FOO_USER");

    Assert.assertEquals(1, source.getFilteredRowCount());
    Assert.assertEquals(1, target.getFilteredRowCount());
  }
}
