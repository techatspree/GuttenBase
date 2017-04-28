package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultDatabaseTableFilter;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.DatabaseTableFilter;
import de.akquinet.jbosscc.guttenbase.tools.AbstractGuttenBaseTest;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Filters columns when inquiring the data base.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DatabaseTableFilterHintTest extends AbstractGuttenBaseTest
{
  public static final String SOURCE = "SOURCE";

  @Before
  public final void setupTables() throws Exception
  {
    _connectorRepository.addConnectionInfo(SOURCE, new TestHsqlConnectionInfo());
    new ScriptExecutorTool(_connectorRepository).executeFileScript(SOURCE, "/ddl/tables.sql");

    _connectorRepository.addConnectorHint(SOURCE, new DatabaseTableFilterHint()
    {
      @Override
      public DatabaseTableFilter getValue()
      {
        return new DefaultDatabaseTableFilter()
        {
          @Override
          public boolean accept(final TableMetaData table) throws SQLException
          {
            return table.getTableName().toUpperCase().contains("USER");
          }
        };
      }
    });
  }

  @Test
  public void testFilter() throws Exception
  {
    final List<TableMetaData> tableMetaData = _connectorRepository.getDatabaseMetaData(SOURCE).getTableMetaData();

    assertEquals(3, tableMetaData.size());

    for (final TableMetaData table : tableMetaData)
    {
      assertTrue(table.getTableName().toUpperCase().contains("USER"));
      assertEquals("TABLE", table.getTableType());
    }
  }
}
