package de.akquinet.jbosscc.guttenbase.connector;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

import java.io.Serializable;
import java.sql.Connection;

/**
 * Information about connectors, in particular, parameters needed to establish a {@link Connection} to the data base.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public interface ConnectorInfo extends Serializable {
  /**
   * @return Data base user
   */
  String getUser();

  /**
   * @return Data base password
   */
  String getPassword();

  /**
   * @return Data base schema
   */
  String getSchema();

  /**
   * @return Data base type
   */
  DatabaseType getDatabaseType();

  /**
   * Create connector
   */
  Connector createConnector(final ConnectorRepository connectorRepository, final String connectorId);
}