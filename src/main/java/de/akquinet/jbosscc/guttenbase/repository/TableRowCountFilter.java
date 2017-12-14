package de.akquinet.jbosscc.guttenbase.repository;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.impl.DatabaseMetaDataInspectorTool;
import java.sql.SQLException;

/**
 * Some tables are really big and computing the row count may take too long for the data base.
 * <p></p>
 * Using this hint the @see {@link DatabaseMetaDataInspectorTool} will compute the row count only
 * for the given tables.
 */
public interface TableRowCountFilter {
  boolean accept(final TableMetaData tableMetaData) throws SQLException;

  int defaultRowCount(final TableMetaData tableMetaData) throws SQLException;
}
