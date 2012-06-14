package de.akquinet.jbosscc.guttenbase.hints;

import org.junit.Before;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;

/**
 * Test a schema migration where ID columns have been renamed. ID in table USER became USER_ID, ID in table COMPANY became COMPANY_ID and so
 * on.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ColumnMapperHintTest extends AbstractHintTest {
	public ColumnMapperHintTest() {
		super("/ddl/tables.sql", "/ddl/tables-id-columns-renamed.sql", "/data/test-data.sql");
	}

	@Before
	public void setup() throws Exception {
		_connectorRepository.addConnectorHint(TARGET, new ColumnMapperHint() {
			@Override
			public ColumnMapper getValue() {
				return new TestTableColumnMapper();
			}
		});
	}
}
