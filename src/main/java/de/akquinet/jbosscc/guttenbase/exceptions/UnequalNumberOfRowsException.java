package de.akquinet.jbosscc.guttenbase.exceptions;

import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;

import java.sql.SQLException;

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
public class UnequalNumberOfRowsException extends SQLException {
  private static final long serialVersionUID = 1L;

  public UnequalNumberOfRowsException(final String reason) {
    super(reason);
  }
}