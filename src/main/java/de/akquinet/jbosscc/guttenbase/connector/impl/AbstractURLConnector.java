package de.akquinet.jbosscc.guttenbase.connector.impl;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connection info via explicit URL and driver.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public abstract class AbstractURLConnector extends AbstractConnector {
	protected static final Logger LOG = Logger.getLogger(AbstractURLConnector.class);

	private final URLConnectorInfo _urlConnectionInfo;

	public AbstractURLConnector(final ConnectorRepository connectorRepository, final String connectorId,
			final URLConnectorInfo urlConnectionInfo) {
		super(connectorRepository, connectorId, urlConnectionInfo);

		assert urlConnectionInfo != null : "urlConnectionInfo != null";
		_urlConnectionInfo = urlConnectionInfo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Connection openConnection() throws SQLException {
		if (_connection == null || _connection.isClosed()) {
			try {
				Class.forName(_urlConnectionInfo.getDriver()).newInstance();
			} catch (final Exception e) {
				LOG.error("JDBC driver not found", e);
				throw new SQLException("Creating JDBC driver", e);
			}

			_connection = DriverManager
					.getConnection(_urlConnectionInfo.getUrl(), _urlConnectionInfo.getUser(), _urlConnectionInfo.getPassword());
		}

		return _connection;
	}
}