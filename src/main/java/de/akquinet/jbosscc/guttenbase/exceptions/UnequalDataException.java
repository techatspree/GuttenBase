package de.akquinet.jbosscc.guttenbase.exceptions;

import de.akquinet.jbosscc.guttenbase.connector.GuttenBaseException;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;

/**
 * Thrown when table data is checked for equality.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 * @see CheckEqualTableDataTool
 */
public class UnequalDataException extends GuttenBaseException {
  private static final long serialVersionUID = 1L;

  public UnequalDataException(final String reason) {
    super(reason);
  }
}