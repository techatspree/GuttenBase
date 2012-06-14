package de.akquinet.jbosscc.guttenbase.tools;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;
import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.connector.Connector;
import de.akquinet.jbosscc.guttenbase.exceptions.TableConfigurationException;
import de.akquinet.jbosscc.guttenbase.hints.NumberOfRowsPerInsertionHint;
import de.akquinet.jbosscc.guttenbase.utils.Util;

public abstract class AbstractTableCopyToolTest extends AbstractGuttenBaseTest {
	public static final String CONNECTOR_ID1 = "hsqldb";
	public static final String CONNECTOR_ID2 = "derby";

	public static final byte[] IMAGE = loadImage();

	protected abstract AbstractTableCopyTool getCopyTool();

	private static byte[] loadImage() {
		try {
			final InputStream stream = Util.getResourceAsStream("/data/test.gif");

			final int available = stream.available();
			final byte[] buf = new byte[available];

			stream.read(buf);

			return buf;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

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
					public int getNumberOfRowsPerInsertion() {
						return 2;
					}

					@Override
					public boolean useValuesClauses() {
						return true;
					}
				};
			}
		});

		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, "/ddl/tables.sql");
		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, false, false, "/data/test-data.sql");

		final Connector connector = _connectorRepository.createConnector(CONNECTOR_ID1);
		final Connection connection = connector.openConnection();
		final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO FOO_DATA (ID, SOME_DATA) VALUES(?, ?);");
		preparedStatement.setLong(1, 1);
		preparedStatement.setBinaryStream(2, new ByteArrayInputStream(IMAGE));
		preparedStatement.executeUpdate();
		connector.closeConnection();

		getCopyTool().copyTables(CONNECTOR_ID1, CONNECTOR_ID2);

		new CheckEqualTableDataTool(_connectorRepository).checkTableData(CONNECTOR_ID1, CONNECTOR_ID2);
	}
}
