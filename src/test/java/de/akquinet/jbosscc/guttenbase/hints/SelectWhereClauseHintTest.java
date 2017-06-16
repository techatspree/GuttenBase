package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.tools.SelectWhereClause;
import org.junit.Assert;
import org.junit.Before;

/**
 * Test a schema migration wheer data will be omited using a WHERE clause
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class SelectWhereClauseHintTest extends AbstractHintTest {
  public SelectWhereClauseHintTest() {
    super("/ddl/tables.sql", "/ddl/tables.sql", "/data/test-data.sql");
  }

  @Before
  public void setup() throws Exception {
    _connectorRepository.addConnectorHint(SOURCE, new SelectWhereClauseHint() {
      @Override
      public SelectWhereClause getValue() {
        return tableMetaData -> {
          switch (tableMetaData.getTableName()) {
            case "FOO_USER":
              return "WHERE ID <= 3";
            case "FOO_USER_COMPANY":
            case "FOO_USER_ROLES":
              return "WHERE USER_ID <= 3";

            default:
              return "";
          }
        };
      }
    });
  }

  @Override
  protected void executeChecks() throws Exception {
    final TableMetaData source = _connectorRepository.getDatabaseMetaData(SOURCE).getTableMetaData("FOO_USER");
    final TableMetaData target = _connectorRepository.getDatabaseMetaData(TARGET).getTableMetaData("FOO_USER");

    Assert.assertEquals(5, source.getTotalRowCount());
    Assert.assertEquals(3, source.getFilteredRowCount());
    Assert.assertEquals(3, target.getTotalRowCount());
    Assert.assertEquals(3, target.getFilteredRowCount());
  }
}
