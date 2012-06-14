package de.akquinet.jbosscc.guttenbase.configuration.impl;

import java.sql.Connection;
import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.configuration.SourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * (Almost) empty implementation
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultSourceDatabaseConfiguration extends AbstractDatabaseConfiguration implements SourceDatabaseConfiguration {
	private static final long serialVersionUID = 1L;

	public DefaultSourceDatabaseConfiguration(final ConnectorRepository connectorRepository) {
		super(connectorRepository);
	}

	/**
	 * Connection is set read only and autocommit is false.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void initializeSourceConnection(final Connection connection, final String connectorId) throws SQLException {
		connection.setAutoCommit(false);
		connection.setReadOnly(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finalizeSourceConnection(final Connection connection, final String connectorId) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeSelect(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterSelect(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException {
	}
}
