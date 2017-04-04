package de.akquinet.jbosscc.guttenbase.hints;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.DatabaseColumnFilter;
import de.akquinet.jbosscc.guttenbase.tools.AbstractGuttenBaseTest;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;

/**
 * Filters column when inquiring the data base.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DatabaseColumnFilterHintTest extends AbstractGuttenBaseTest
{
  public static final String SOURCE = "SOURCE";

  @Before
  public final void setupTables() throws Exception
  {
    _connectorRepository.addConnectionInfo(SOURCE, new TestHsqlConnectionInfo());
    new ScriptExecutorTool(_connectorRepository).executeFileScript(SOURCE, "/ddl/tables.sql");
  }

  @Test
  public void testDefault() throws Exception
  {
    final TableMetaData tableMetaData = _connectorRepository.getDatabaseMetaData(SOURCE).getTableMetaData("FOO_USER");

    assertNotNull(tableMetaData);
    assertNotNull(tableMetaData.getColumnMetaData("USERNAME"));
    assertNotNull(tableMetaData.getColumnMetaData("PASSWORD"));
  }

  @Test
  public void testFilter() throws Exception
  {
    _connectorRepository.addConnectorHint(SOURCE, new DatabaseColumnFilterHint()
    {
      @Override
      public DatabaseColumnFilter getValue()
      {
        return columnMetaData -> !columnMetaData.getTableMetaData().getTableName().equals("FOO_USER") || !columnMetaData.getColumnName()
            .equals("PASSWORD");
      }
    });

    final TableMetaData tableMetaData = _connectorRepository.getDatabaseMetaData(SOURCE).getTableMetaData("FOO_USER");

    assertNotNull(tableMetaData);
    assertNotNull(tableMetaData.getColumnMetaData("USERNAME"));
    assertNull(tableMetaData.getColumnMetaData("PASSWORD"));
  }
}
