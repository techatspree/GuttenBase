package de.akquinet.jbosscc.guttenbase.hints;

import org.junit.Before;

import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;

/**
 * Test a schema migration where table names contains spaces and thus need to be escaped with double quotes ("")
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class TableNameMapperHintTest extends AbstractHintTest {
	public TableNameMapperHintTest() {
		super("/ddl/tables-with-spaces.sql", "/ddl/tables-with-spaces.sql", "/data/test-data-with-spaces.sql");
	}

	@Before
	public void setup() throws Exception {
		_connectorRepository.addConnectorHint(SOURCE, new TableNameMapperHint() {
			@Override
			public TableNameMapper getValue() {
				return new TestTableNameMapper();
			}
		});

		_connectorRepository.addConnectorHint(TARGET, new TableNameMapperHint() {
			@Override
			public TableNameMapper getValue() {
				return new TestTableNameMapper();
			}
		});
	}
}
