package de.akquinet.jbosscc.guttenbase.exceptions;

import java.sql.SQLException;

/**
 * Thrown when there is an error while reading the data from source data base.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class MissingDataException extends SQLException {
  private static final long serialVersionUID = 1L;

  public MissingDataException(final String reason) {
    super(reason);
  }
}