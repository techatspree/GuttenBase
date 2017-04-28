package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import org.junit.Before;

/**
 * Test a schema migration where table names contains spaces and thus need to be escaped with double quotes ("")
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class TableNameMapperHintTest extends AbstractHintTest
{
  public TableNameMapperHintTest()
  {
    super("/ddl/tables-with-spaces.sql", "/ddl/tables-with-spaces.sql", "/data/test-data-with-spaces.sql");
  }

  @Before
  public void setup() throws Exception
  {
    _connectorRepository.addConnectorHint(SOURCE, new TableMapperHint()
    {
      @Override
      public TableMapper getValue()
      {
        return new TestTableNameMapper();
      }
    });

    _connectorRepository.addConnectorHint(TARGET, new TableMapperHint()
    {
      @Override
      public TableMapper getValue()
      {
        return new TestTableNameMapper();
      }
    });
  }
}
