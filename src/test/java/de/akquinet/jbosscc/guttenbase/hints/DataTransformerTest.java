package de.akquinet.jbosscc.guttenbase.hints;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.Before;

import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultColumnDataMapperProvider;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultColumnDataMapperProviderHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnDataMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;

/**
 * Test a schema migration where all strings are converted.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DataTransformerTest extends AbstractHintTest {
	private static final String SUFFIX = " (converted)";

	public DataTransformerTest() {
		super("/ddl/tables.sql", "/ddl/tables.sql", "/data/test-data.sql");
	}

	@Before
	public void setup() throws Exception {
		final ColumnDataMapper columnDataMapper = new ColumnDataMapper() {
			@Override
			public boolean isApplicable(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData) throws SQLException {
				return sourceColumnMetaData.getColumnName().toUpperCase().endsWith("NAME");
			}

			@Override
			public Object map(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData, final Object value)
					throws SQLException {
				return value + SUFFIX;
			}
		};

		_connectorRepository.addConnectorHint(TARGET, new DefaultColumnDataMapperProviderHint() {
			@Override
			protected void addMappings(final DefaultColumnDataMapperProvider columnDataMapperFactory) {
				super.addMappings(columnDataMapperFactory);
				columnDataMapperFactory.addMapping(ColumnType.CLASS_STRING, ColumnType.CLASS_STRING, columnDataMapper);
			}
		});
	}

	@Override
	protected void executeChecks() throws Exception {
		final List<Map<String, Object>> list = new ScriptExecutorTool(_connectorRepository).executeQuery(TARGET,
				"SELECT DISTINCT ID, USERNAME, NAME, PASSWORD FROM FOO_USER ORDER BY ID");

		assertEquals(5, list.size());

		final String name = (String) list.get(0).get("NAME");
		final String userName = (String) list.get(0).get("USERNAME");
		final String password = (String) list.get(0).get("PASSWORD");

		assertEquals("User_1" + SUFFIX, userName);
		assertEquals("User 1" + SUFFIX, name);
		assertEquals("secret", password);
	}
}
