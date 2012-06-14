package de.akquinet.jbosscc.guttenbase.configuration.impl;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Implementation for H2 DB data base.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class H2DbSourceDatabaseConfiguration extends DefaultSourceDatabaseConfiguration {
  private static final long serialVersionUID = 1L;

  public H2DbSourceDatabaseConfiguration(final ConnectorRepository connectorRepository) {
    super(connectorRepository);
  }
}
