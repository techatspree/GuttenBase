package de.akquinet.jbosscc.guttenbase.hints;

import org.junit.Before;

import de.akquinet.jbosscc.guttenbase.hints.impl.DroppingColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;

/**
 * Test a schema migration where ID columns have been renamed. ID in table USER became USER_ID, ID in table COMPANY became
 * COMPANY_ID and so on.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DroppingColumnMapperHintTest extends AbstractHintTest
{
  public DroppingColumnMapperHintTest()
  {
    super("/ddl/tables.sql", "/ddl/tables-missingColumn.sql", "/data/test-data.sql");
  }

  @Before
  public void setup() throws Exception
  {
    _connectorRepository.addConnectorHint(TARGET, new ColumnMapperHint()
    {
      @Override
      public ColumnMapper getValue()
      {
        return new DroppingColumnMapper().addDroppedColumn("FOO_USER", "PERSONAL_NUMBER");
      }
    });

    //    _connectorRepository.addConnectorHint(TARGET, new DataColumnMapperHint()
  }
}
