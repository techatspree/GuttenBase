package de.akquinet.jbosscc.guttenbase.exceptions;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.repository.RepositoryTableFilter;

/**
 * Thrown when data bases have not the same tables. You can omit tables deliberately using the {@link RepositoryTableFilter} hint.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class TableConfigurationException extends SQLException {
  private static final long serialVersionUID = 1L;

  public TableConfigurationException(final String reason) {
    super(reason);
  }
}