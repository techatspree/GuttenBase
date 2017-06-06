package de.akquinet.jbosscc.guttenbase.exceptions;

import java.sql.SQLException;

/**
 * Thrown when tables do not match.
 * <p/>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
@SuppressWarnings("deprecation")
public class IncompatibleTablesException extends SQLException {
  private static final long serialVersionUID = 1L;

  public IncompatibleTablesException(final String reason) {
    super(reason);
  }
}
