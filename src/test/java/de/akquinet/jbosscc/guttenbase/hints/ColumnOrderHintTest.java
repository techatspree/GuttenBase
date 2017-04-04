package de.akquinet.jbosscc.guttenbase.hints;

import java.util.Comparator;

import org.junit.Before;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnOrderComparatorFactory;

/**
 * Test a schema migration where column ordering is customized...
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
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
