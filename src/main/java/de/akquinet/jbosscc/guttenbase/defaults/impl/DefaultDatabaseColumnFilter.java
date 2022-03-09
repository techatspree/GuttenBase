package de.akquinet.jbosscc.guttenbase.defaults.impl;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.repository.DatabaseColumnFilter;

public class DefaultDatabaseColumnFilter implements DatabaseColumnFilter {
  @Override
  public boolean accept(final ColumnMetaData columnMetaData) {
    return true;
  }
}
