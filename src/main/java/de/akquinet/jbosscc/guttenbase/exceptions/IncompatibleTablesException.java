package de.akquinet.jbosscc.guttenbase.exceptions;

import de.akquinet.jbosscc.guttenbase.connector.GuttenBaseException;

/**
 * Thrown when tables do not match.
 * <p></p>
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
@SuppressWarnings("deprecation")
public class IncompatibleTablesException extends GuttenBaseException {
  private static final long serialVersionUID = 1L;

  public IncompatibleTablesException(final String reason) {
    super(reason);
  }
}
