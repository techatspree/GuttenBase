package de.akquinet.jbosscc.guttenbase.exceptions;

import java.sql.SQLException;

/**
 * Thrown when we find a column type we cannot handle (yet).
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class UnhandledColumnTypeException extends SQLException {
  private static final long serialVersionUID = 1L;

  public UnhandledColumnTypeException(final String reason) {
    super(reason);
  }
}