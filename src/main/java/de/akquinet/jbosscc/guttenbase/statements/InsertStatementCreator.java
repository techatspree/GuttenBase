package de.akquinet.jbosscc.guttenbase.statements;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Create INSERT statement with multiple VALUES-tuples.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class InsertStatementCreator extends AbstractInsertStatementCreator {
  public InsertStatementCreator(final ConnectorRepository connectorRepository, final String connectorId) {
    super(connectorRepository, connectorId);
  }
}
