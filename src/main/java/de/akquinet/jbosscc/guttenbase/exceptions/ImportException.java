package de.akquinet.jbosscc.guttenbase.exceptions;

import de.akquinet.jbosscc.guttenbase.export.ImportDumpConnector;

import java.io.IOException;
import java.sql.SQLException;

/**
 * "Fake" {@link SQLException} in order to encapsulate {@link IOException} thrown during dumping or restoring data bases using
 * {@link ImportDumpConnector}
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ImportException extends SQLException {
  private static final long serialVersionUID = 1L;

  public ImportException(final String reason, final Exception e) {
    super(reason, e);
  }

  public ImportException(final String reason) {
    super(reason);
  }
}