package de.akquinet.jbosscc.guttenbase.exceptions;

import java.sql.SQLException;

/**
 * Thrown when tables in different data bases exist, but have different columns.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class IncompatibleTablesException extends SQLException {
  private static final long serialVersionUID = 1L;

  public IncompatibleTablesException(final String reason) {
    super(reason);
  }
}