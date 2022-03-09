package de.akquinet.jbosscc.guttenbase.hints;

import org.junit.Before;

/**
 * Test a schema migration where table ordering is customized...
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class TableOrderHintTest extends AbstractHintTest {
  public TableOrderHintTest() {
    super("/ddl/tables.sql", "/ddl/tables.sql", "/data/test-data.sql");
  }

  @Before
  public void setup() throws Exception {
    _connectorRepository.addConnectorHint(TARGET, new RandomTableOrderHint());
  }
}
