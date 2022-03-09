package de.akquinet.jbosscc.guttenbase.configuration.impl;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Implementation for IBM DB2 data base.
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class Db2SourceDatabaseConfiguration extends DefaultSourceDatabaseConfiguration {
  public Db2SourceDatabaseConfiguration(final ConnectorRepository connectorRepository) {
    super(connectorRepository);
  }
}
