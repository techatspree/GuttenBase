package de.akquinet.jbosscc.guttenbase.connector.impl;

import de.akquinet.jbosscc.guttenbase.connector.ConnectorInfo;

/**
 * Connector info via explicit URL and driver.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public interface URLConnectorInfo extends ConnectorInfo {
	String getUrl();

	String getDriver();
}