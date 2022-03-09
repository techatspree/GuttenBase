package de.akquinet.jbosscc.guttenbase.exceptions;

import de.akquinet.jbosscc.guttenbase.connector.GuttenBaseException;
import de.akquinet.jbosscc.guttenbase.statements.InsertStatementFiller;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;

/**
 * Thrown when tables have mismatching columns.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 * @see InsertStatementFiller
 * @see SchemaComparatorTool
 * @see CheckEqualTableDataTool
 */
@SuppressWarnings("deprecation")
public class IncompatibleColumnsException extends GuttenBaseException {
  private static final long serialVersionUID = 1L;

  public IncompatibleColumnsException(final String reason) {
    super(reason);
  }
}
