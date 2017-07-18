package de.akquinet.jbosscc.guttenbase.defaults.impl;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.TableRowCountFilter;
import java.sql.SQLException;

/**
 * By default compute row count for all tables
 */
public class DefaultTableRowCountFilter implements TableRowCountFilter {
  @Override
  public boolean accept(final TableMetaData tableMetaData) throws SQLException {
    return true;
  }

  @Override
  public int defaultRowCount(final TableMetaData tableMetaData) throws SQLException {
    return 0;
  }
}
