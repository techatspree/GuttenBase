package de.akquinet.jbosscc.guttenbase.hints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.Before;

import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultColumnDataMapperProvider;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultColumnDataMapperProviderHint;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;

/**
 * Test a schema migration where all BIGINT IDs are converted to UUID strings.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ColumnDataMapperProviderHintTest extends AbstractHintTest {
	public ColumnDataMapperProviderHintTest() {
		super("/ddl/tables.sql", "/ddl/tables-uuid.sql", "/data/test-data.sql");
	}

	@Before
	public void setup() throws Exception {
		final TestUUIDColumnDataMapper columnDataMapper = new TestUUIDColumnDataMapper();

		_connectorRepository.addConnectorHint(TARGET, new DefaultColumnDataMapperProviderHint() {
			@Override
			protected void addMappings(final DefaultColumnDataMapperProvider columnDataMapperFactory) {
				super.addMappings(columnDataMapperFactory);
				columnDataMapperFactory.addMapping(ColumnType.CLASS_LONG, ColumnType.CLASS_STRING, columnDataMapper);
				columnDataMapperFactory.addMapping(ColumnType.CLASS_BIGDECIMAL, ColumnType.CLASS_STRING, columnDataMapper);
			}
		});
	}

	@Override
	protected void executeChecks() throws Exception {
		final List<Map<String, Object>> list = new ScriptExecutorTool(_connectorRepository).executeQuery(TARGET,
				"SELECT DISTINCT ID FROM FOO_USER ORDER BY ID");

		assertEquals(5, list.size());

		final Object id = list.get(0).get("ID");
		assertNotNull(id);

		// Given that String#hashCode is deterministic
		assertEquals("ffffffff-88e7-1891-0000-000000000001", id);
	}
}
