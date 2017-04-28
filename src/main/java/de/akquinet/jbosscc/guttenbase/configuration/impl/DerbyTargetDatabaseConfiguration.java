package de.akquinet.jbosscc.guttenbase.configuration.impl;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Implementation for Derby data base. Derby does not support tompariliy disabling (FK) constraints
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DerbyTargetDatabaseConfiguration extends DefaultTargetDatabaseConfiguration {
	public DerbyTargetDatabaseConfiguration(final ConnectorRepository connectorRepository) {
		super(connectorRepository);
	}
}
