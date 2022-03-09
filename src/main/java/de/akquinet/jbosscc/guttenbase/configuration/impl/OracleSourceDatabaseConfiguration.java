package de.akquinet.jbosscc.guttenbase.configuration.impl;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Implementation for Oracle data base.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class OracleSourceDatabaseConfiguration extends DefaultSourceDatabaseConfiguration {
	public OracleSourceDatabaseConfiguration(final ConnectorRepository connectorRepository) {
		super(connectorRepository);
	}
}
