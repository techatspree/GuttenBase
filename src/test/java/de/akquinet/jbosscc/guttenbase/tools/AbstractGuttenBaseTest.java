package de.akquinet.jbosscc.guttenbase.tools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Before;

import de.akquinet.jbosscc.guttenbase.connector.Connector;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.utils.Util;

@SuppressWarnings("ConstantConditions")
public abstract class AbstractGuttenBaseTest {
	/**
	 * Place all DB data in temporary directory. Pure in-memory DBs are faster but mess up when running multiple tests.
	 */
	public static final File DB_DIRECTORY = new File("target/db");
	public static final byte[] IMAGE = loadImage();

	protected final ConnectorRepository _connectorRepository = new ConnectorRepositoryImpl();

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
	public void dropTables() {
		Util.deleteDirectory(DB_DIRECTORY);
	}

	protected void insertBinaryData(final String connectorId, final int dataId) throws SQLException {
		final Connector connector = _connectorRepository.createConnector(connectorId);
		final Connection connection = connector.openConnection();
		final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO FOO_DATA (ID, SOME_DATA) VALUES(?, ?)");
		preparedStatement.setLong(1, dataId);
		preparedStatement.setBinaryStream(2, new ByteArrayInputStream(IMAGE));
		preparedStatement.executeUpdate();
		connector.closeConnection();
	}
}
