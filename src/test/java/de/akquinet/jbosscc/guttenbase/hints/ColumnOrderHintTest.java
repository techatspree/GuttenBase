package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnOrderComparatorFactory;
import org.junit.Before;

import java.util.Comparator;

/**
 * Test a schema migration where column ordering is customized...
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ColumnOrderHintTest extends AbstractHintTest {
  public ColumnOrderHintTest() {
    super("/ddl/tables.sql", "/ddl/tables.sql", "/data/test-data.sql");
  }

  @Before
  public void setup() throws Exception {
    _connectorRepository.addConnectorHint(SOURCE, new ColumnOrderHint() {
      @Override
      public ColumnOrderComparatorFactory getValue() {
        return () -> Comparator.comparingInt(Object::hashCode);
      }
    });
  }
}
