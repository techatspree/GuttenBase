package de.akquinet.jbosscc.guttenbase.exceptions;

import de.akquinet.jbosscc.guttenbase.export.ExportDumpConnector;

import java.io.IOException;
import java.sql.SQLException;

/**
 * "Fake" {@link SQLException} in order to encapsulate {@link IOException} thrown during dumping or restoring data bases using
 * {@link ExportDumpConnector}
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ExportException extends SQLException {
  private static final long serialVersionUID = 1L;

  public ExportException(final String reason, final Exception e) {
    super(reason, e);
  }

  public ExportException(final String reason) {
    super(reason);
  }
}