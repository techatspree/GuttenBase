package de.akquinet.jbosscc.guttenbase.tools;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;
import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.exceptions.TableConfigurationException;
import de.akquinet.jbosscc.guttenbase.hints.NumberOfRowsPerBatchHint;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

public abstract class AbstractTableCopyToolTest extends AbstractGuttenBaseTest {
	public static final String CONNECTOR_ID1 = "hsqldb";
	public static final String CONNECTOR_ID2 = "derby";

	@Before
	public void setup() {
		_connectorRepository.addConnectionInfo(CONNECTOR_ID1, new TestHsqlConnectionInfo());
		_connectorRepository.addConnectionInfo(CONNECTOR_ID2, new TestDerbyConnectionInfo());
	}

	@Test(expected = TableConfigurationException.class)
	public void testCopyFailsMissingTables() throws Exception {
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, false, false, "/data/test-data.sql");

		getCopyTool().copyTables(CONNECTOR_ID1, CONNECTOR_ID2);
	}

	@Test
	public void testCopyWithMultipleValuesClauses() throws Exception {
		addNumberOfRowsPerBatchHint(2, true);

		setupSourceData();

		getCopyTool().copyTables(CONNECTOR_ID1, CONNECTOR_ID2);

		new CheckEqualTableDataTool(_connectorRepository).checkTableData(CONNECTOR_ID1, CONNECTOR_ID2);
	}

	@Test
	public void testCopyWithBatchMode() throws Exception {
		addNumberOfRowsPerBatchHint(2, false);

		setupSourceData();

		getCopyTool().copyTables(CONNECTOR_ID1, CONNECTOR_ID2);

		new CheckEqualTableDataTool(_connectorRepository).checkTableData(CONNECTOR_ID1, CONNECTOR_ID2);
	}

	private void addNumberOfRowsPerBatchHint(final int numberOfRowsPerBatch, final boolean useMultipleValuesClauses) {
		_connectorRepository.addConnectorHint(CONNECTOR_ID1, new NumberOfRowsPerBatchHint() {
			@Override
			public NumberOfRowsPerBatch getValue() {
				return new NumberOfRowsPerBatch() {
					@Override
					public int getNumberOfRowsPerBatch(final TableMetaData targetTableMetaData) {
						return numberOfRowsPerBatch;
					}

					@Override
					public boolean useMultipleValuesClauses(final TableMetaData targetTableMetaData) {
						return useMultipleValuesClauses;
					}
				};
			}
		});
	}

	private void setupSourceData() throws SQLException {
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, false, false, "/data/test-data.sql");

		for (int i = 1; i < 5; i++) {
			insertBinaryData(CONNECTOR_ID1, i);
		}
	}

	protected abstract AbstractTableCopyTool getCopyTool();
}
