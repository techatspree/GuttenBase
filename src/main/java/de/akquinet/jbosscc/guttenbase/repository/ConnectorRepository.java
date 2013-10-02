package de.akquinet.jbosscc.guttenbase.repository;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.configuration.SourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.TargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.connector.Connector;
import de.akquinet.jbosscc.guttenbase.connector.ConnectorInfo;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.hints.ConnectorHint;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;

/**
 * The main repository containing all configured connectors.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface ConnectorRepository extends Serializable {

	/**
	 * Add connection info to repository with symbolic ID for data base such as "source db", e.g.
	 */
	void addConnectionInfo(final String connectorId, final ConnectorInfo connectionInfo);

	/**
	 * Remove all information about connector
	 */
	void removeConnectionInfo(final String connectorId);

	/**
	 * Get connection info
	 */
	ConnectorInfo getConnectionInfo(final String connectorId);

	/**
	 * Get all meta data from data base.
	 */
	DatabaseMetaData getDatabaseMetaData(final String connectorId) throws SQLException;

	/**
	 * Reset table data, i.e. it will be reread from the data base.
	 */
	void refreshDatabaseMetaData(final String connectorId);

	/**
	 * Create connector
	 */
	Connector createConnector(final String connectorId);

	/**
	 * Get configuration.
	 */
	SourceDatabaseConfiguration getSourceDatabaseConfiguration(final String connectorId);

	/**
	 * Get configuration.
	 */
	TargetDatabaseConfiguration getTargetDatabaseConfiguration(final String connectorId);

	/**
	 * Add configuration hint for connector.
	 */
	<T> void addConnectorHint(final String connectorId, final ConnectorHint<T> hint);

	/**
	 * Remove configuration hint for connector.
	 */
	<T> void removeConnectorHint(final String connectorId, final Class<T> connectionInfoHintType);

	/**
	 * Get configuration hint for connector.
	 */
	<T> ConnectorHint<T> getConnectorHint(final String connectorId, final Class<T> connectorHintType);

	/**
	 * Get all currently configured connector IDs.
	 */
	List<String> getConnectorIds();

	/**
	 * Define configuration for given data base type when reading data.
	 */
	void addSourceDatabaseConfiguration(DatabaseType databaseType, SourceDatabaseConfiguration sourceDatabaseConfiguration);

	/**
	 * Define configuration for given data base type when writing data.
	 */
	void addTargetDatabaseConfiguration(DatabaseType databaseType, TargetDatabaseConfiguration targetDatabaseConfiguration);
}