package de.akquinet.jbosscc.guttenbase.hints.impl;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.DatabaseColumnFilter;

public class DefaultDatabaseColumnFilter implements DatabaseColumnFilter
{
  @Override
  public boolean accept(final TableMetaData table, final ColumnMetaData columnMetaData) throws SQLException
  {
    return true;
  }
}
