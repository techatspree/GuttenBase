package de.akquinet.jbosscc.guttenbase.configuration.impl;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Implementation for MS Access via UCanAccess.
 *
 * http://ucanaccess.sourceforge.net/site.html
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class MsAccessTargetDatabaseConfiguration extends DefaultTargetDatabaseConfiguration {
	public MsAccessTargetDatabaseConfiguration(final ConnectorRepository connectorRepository) {
		super(connectorRepository);
	}
}
