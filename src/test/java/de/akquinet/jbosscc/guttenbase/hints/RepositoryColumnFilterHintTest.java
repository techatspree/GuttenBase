package de.akquinet.jbosscc.guttenbase.hints;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryColumnFilter;
import de.akquinet.jbosscc.guttenbase.tools.AbstractGuttenBaseTest;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;

/**
 * Filters columns when inquiring connector repository.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class RepositoryColumnFilterHintTest extends AbstractGuttenBaseTest
{
  public static final String SOURCE = "SOURCE";

  @Before
  public final void setupTables() throws Exception
  {
    _connectorRepository.addConnectionInfo(SOURCE, new TestHsqlConnectionInfo());
    new ScriptExecutorTool(_connectorRepository).executeFileScript(SOURCE, "/ddl/tables.sql");
  }

  @Test
  public void testFilter() throws Exception
  {
    assertEquals("Before", 6, _connectorRepository.getDatabaseMetaData(SOURCE).getTableMetaData("FOO_USER").getColumnCount());

    _connectorRepository.addConnectorHint(SOURCE, new RepositoryColumnFilterHint()
    {
      @Override
      public RepositoryColumnFilter getValue()
      {
        return column -> !column.getColumnName().equalsIgnoreCase("password");
      }
    });

    assertEquals("After", 5, _connectorRepository.getDatabaseMetaData(SOURCE).getTableMetaData("FOO_USER").getColumnCount());
  }
}
