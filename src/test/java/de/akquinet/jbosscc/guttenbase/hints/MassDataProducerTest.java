package de.akquinet.jbosscc.guttenbase.hints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;
import de.akquinet.jbosscc.guttenbase.configuration.TestH2ConnectionInfo;
import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultColumnDataMapperProvider;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultColumnDataMapperProviderHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnDataMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.tools.AbstractGuttenBaseTest;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.EntityTableChecker;
import de.akquinet.jbosscc.guttenbase.tools.MinMaxIdSelectorTool;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;

/**
 * Produce lots of data by duplicating and altering existing entries. IDs have to be adapted of course.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class MassDataProducerTest extends AbstractGuttenBaseTest {
	private static final int MAX_LOOP = 5;
	public static final String SOURCE = "SOURCE";
	public static final String TARGET = "TARGET";

	private final ColumnDataMapper _nameDataMapper = new ColumnDataMapper() {
		@Override
		public boolean isApplicable(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData) throws SQLException {
			return sourceColumnMetaData.getColumnName().toUpperCase().endsWith("NAME");
		}

		@Override
		public Object map(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData, final Object value)
				throws SQLException {
			return value + "_" + _loopCounter;
		}
	};

	private final ColumnDataMapper _idDataMapper = new ColumnDataMapper() {
		@Override
		public boolean isApplicable(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData) throws SQLException {
			return sourceColumnMetaData.getColumnName().toUpperCase().endsWith("ID");
		}

		@Override
		public Object map(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData, final Object value)
				throws SQLException {
			return ((Long) value) + getOffset(sourceColumnMetaData);
		}
	};

	private final Map<TableMetaData, Long> _maxTableIds = new HashMap<>();
	private int _loopCounter;

	@Before
	public void setup() throws Exception {
		_connectorRepository.addConnectionInfo(SOURCE, new TestDerbyConnectionInfo());
		_connectorRepository.addConnectionInfo(TARGET, new TestH2ConnectionInfo());
		new ScriptExecutorTool(_connectorRepository).executeFileScript(SOURCE, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(TARGET, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(SOURCE, false, false, "/data/test-data.sql");

		_connectorRepository.addConnectorHint(TARGET, new DefaultColumnDataMapperProviderHint() {
			@Override
			protected void addMappings(final DefaultColumnDataMapperProvider columnDataMapperFactory) {
				super.addMappings(columnDataMapperFactory);
				columnDataMapperFactory.addMapping(ColumnType.CLASS_STRING, ColumnType.CLASS_STRING, _nameDataMapper);
				columnDataMapperFactory.addMapping(ColumnType.CLASS_LONG, ColumnType.CLASS_LONG, _idDataMapper);
			}
		});

		computeMaximumIds();
	}

	@Test
	public void testDataDuplicates() throws Exception {
		for (_loopCounter = 0; _loopCounter < MAX_LOOP; _loopCounter++) {
			new DefaultTableCopyTool(_connectorRepository).copyTables(SOURCE, TARGET);
		}

		final List<Map<String, Object>> listUserTable = new ScriptExecutorTool(_connectorRepository).executeQuery(TARGET,
				"SELECT DISTINCT ID, USERNAME, NAME, PASSWORD FROM FOO_USER ORDER BY ID");

		assertEquals(5 * MAX_LOOP, listUserTable.size());
		final List<Map<String, Object>> listUserCompanyTable = new ScriptExecutorTool(_connectorRepository).executeQuery(TARGET,
				"SELECT DISTINCT USER_ID, ASSIGNED_COMPANY_ID FROM FOO_USER_COMPANY ORDER BY USER_ID");

		assertEquals(3 * MAX_LOOP, listUserCompanyTable.size());
	}

	private long getOffset(final ColumnMetaData sourceColumnMetaData) {
		ColumnMetaData idColumnMetaData = sourceColumnMetaData.getReferencedColumn();

		if (idColumnMetaData == null) {
			idColumnMetaData = sourceColumnMetaData;
		}

		final TableMetaData tableMetaData = idColumnMetaData.getTableMetaData();
		final Long maxId = _maxTableIds.get(tableMetaData);

		assertNotNull(sourceColumnMetaData + ":" + tableMetaData, maxId);

		return _loopCounter * maxId;
	}

	private void computeMaximumIds() throws SQLException {
		final List<TableMetaData> tables = _connectorRepository.getDatabaseMetaData(SOURCE).getTableMetaData();
		final EntityTableChecker entityTableChecker = _connectorRepository.getConnectorHint(SOURCE, EntityTableChecker.class).getValue();
		final MinMaxIdSelectorTool minMaxIdSelectorTool = new MinMaxIdSelectorTool(_connectorRepository);

		for (final TableMetaData tableMetaData : tables) {
			if (entityTableChecker.isEntityTable(tableMetaData)) {
				minMaxIdSelectorTool.computeMinMax(SOURCE, tableMetaData);
				_maxTableIds.put(tableMetaData, minMaxIdSelectorTool.getMaxValue());
			}
		}
	}
}
