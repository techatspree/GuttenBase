package de.akquinet.jbosscc.guttenbase.configuration.impl;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Implementation for MS Access via UCanAccess.
 * <p>
 * http://ucanaccess.sourceforge.net/site.html
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class MsAccessSourceDatabaseConfiguration extends DefaultSourceDatabaseConfiguration {
  public MsAccessSourceDatabaseConfiguration(final ConnectorRepository connectorRepository) {
    super(connectorRepository);
  }
}
