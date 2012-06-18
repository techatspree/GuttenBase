package de.akquinet.jbosscc.guttenbase.tools;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;
import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.exceptions.TableConfigurationException;
import de.akquinet.jbosscc.guttenbase.hints.NumberOfRowsPerInsertionHint;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

public abstract class AbstractTableCopyToolTest extends AbstractGuttenBaseTest {
	public static final String CONNECTOR_ID1 = "hsqldb";
	public static final String CONNECTOR_ID2 = "derby";

	protected abstract AbstractTableCopyTool getCopyTool();

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
	public void testCopy() throws Exception {
		_connectorRepository.addConnectorHint(CONNECTOR_ID1, new NumberOfRowsPerInsertionHint() {
			@Override
			public NumberOfRowsPerInsertion getValue() {
				return new NumberOfRowsPerInsertion() {
					@Override
					public int getNumberOfRowsPerInsertion(final TableMetaData targetTableMetaData) {
						return 2;
					}

					@Override
					public boolean useValuesClauses(final TableMetaData targetTableMetaData) {
						return true;
					}
				};
			}
		});

		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, false, false, "/data/test-data.sql");

		for (int i = 1; i < 5; i++) {
			insertBinaryData(CONNECTOR_ID1, i);
		}

		getCopyTool().copyTables(CONNECTOR_ID1, CONNECTOR_ID2);

		new CheckEqualTableDataTool(_connectorRepository).checkTableData(CONNECTOR_ID1, CONNECTOR_ID2);
	}
}
