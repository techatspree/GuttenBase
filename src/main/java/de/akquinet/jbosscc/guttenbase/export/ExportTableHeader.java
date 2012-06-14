package de.akquinet.jbosscc.guttenbase.export;

import java.io.Serializable;

/**
 * Denote start of new table in export file.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface ExportTableHeader extends Serializable {
  String getTableName();
}
