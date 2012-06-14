package de.akquinet.jbosscc.guttenbase.exceptions;

import java.io.IOException;
import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.export.ImportDumpConnector;

/**
 * "Fake" {@link SQLException} in order to encapsulate {@link IOException} thrown during dumping or restoring data bases using
 * {@link ImportDumpConnector}
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ImportException extends SQLException {
  private static final long serialVersionUID = 1L;

  public ImportException(final String reason, final Exception e) {
    super(reason, e);
  }
}