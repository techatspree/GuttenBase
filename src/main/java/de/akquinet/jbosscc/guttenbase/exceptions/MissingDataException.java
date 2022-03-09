package de.akquinet.jbosscc.guttenbase.exceptions;

import de.akquinet.jbosscc.guttenbase.connector.GuttenBaseException;

/**
 * Thrown when there is an error while reading the data from source data base.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class MissingDataException extends GuttenBaseException {
  private static final long serialVersionUID = 1L;

  public MissingDataException(final String reason) {
    super(reason);
  }
}