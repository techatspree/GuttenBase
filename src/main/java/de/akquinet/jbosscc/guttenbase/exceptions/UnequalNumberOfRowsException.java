package de.akquinet.jbosscc.guttenbase.exceptions;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;

/**
 * Thrown when table data is checked for equality.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @see CheckEqualTableDataTool
 * @author M. Dahm
 */
public class UnequalNumberOfRowsException extends SQLException {
  private static final long serialVersionUID = 1L;

  public UnequalNumberOfRowsException(final String reason) {
    super(reason);
  }
}