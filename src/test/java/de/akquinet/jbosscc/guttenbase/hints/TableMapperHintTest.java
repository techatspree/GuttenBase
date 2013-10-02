package de.akquinet.jbosscc.guttenbase.hints;

import org.junit.Before;

import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;

/**
 * Test a schema migration where tables have been renamed, because the source tables contain umlauts which are not always supported...
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class TableMapperHintTest extends AbstractHintTest {
	public TableMapperHintTest() {
		super("/ddl/tables-with-umlauts.sql", "/ddl/tables.sql", "/data/test-data-with-umlauts.sql");
	}

	@Before
	public void setup() throws Exception {
		_connectorRepository.addConnectorHint(TARGET, new TableMapperHint() {
			@Override
			public TableMapper getValue() {
				return new TestTableMapper();
			}
		});
	}
}
