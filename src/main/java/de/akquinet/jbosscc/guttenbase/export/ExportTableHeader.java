package de.akquinet.jbosscc.guttenbase.export;

import java.io.Serializable;

/**
 * Denote start of new table in export file. Provide mininmal information about table.
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public interface ExportTableHeader extends Serializable {
  String getTableName();
}
