package de.akquinet.jbosscc.guttenbase.exceptions;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.statements.InsertStatementFiller;
import de.akquinet.jbosscc.guttenbase.tools.CheckSchemaCompatibilityTool;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;

/**
 * Thrown when tables have mismatching columns.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @see InsertStatementFiller
 * @see CheckSchemaCompatibilityTool
 * @see CheckEqualTableDataTool
 * 
 * @author M. Dahm
 */
public class IncompatibleColumnsException extends SQLException {
  private static final long serialVersionUID = 1L;

  public IncompatibleColumnsException(final String reason) {
    super(reason);
  }
}