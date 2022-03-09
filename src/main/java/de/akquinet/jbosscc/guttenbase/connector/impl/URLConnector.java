package de.akquinet.jbosscc.guttenbase.connector.impl;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import org.apache.log4j.Logger;

/**
 * Connection info via explicit URL and driver.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class URLConnector extends AbstractURLConnector {
	protected static final Logger LOG = Logger.getLogger(URLConnector.class);

	public URLConnector(final ConnectorRepository connectorRepository, final String connectorId, final URLConnectorInfo urlConnectionInfo) {
		super(connectorRepository, connectorId, urlConnectionInfo);
	}
}