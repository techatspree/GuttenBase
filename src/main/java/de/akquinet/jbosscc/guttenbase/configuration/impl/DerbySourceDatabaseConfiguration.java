package de.akquinet.jbosscc.guttenbase.configuration.impl;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Implementation for Derby data base.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DerbySourceDatabaseConfiguration extends DefaultSourceDatabaseConfiguration {
	public DerbySourceDatabaseConfiguration(final ConnectorRepository connectorRepository) {
		super(connectorRepository);
	}
}
