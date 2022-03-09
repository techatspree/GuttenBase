package de.akquinet.jbosscc.guttenbase.defaults.impl;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.repository.DatabaseColumnFilter;

import java.sql.SQLException;

public class DefaultDatabaseColumnFilter implements DatabaseColumnFilter {
  @Override
  public boolean accept(final ColumnMetaData columnMetaData) throws SQLException {
    return true;
  }
}
