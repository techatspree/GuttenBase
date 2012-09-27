package de.akquinet.jbosscc.guttenbase.hints.impl;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.DatabaseTableFilter;

public class DefaultDatabaseTableFilter implements DatabaseTableFilter {
  @Override
  public boolean accept(final TableMetaData table) throws SQLException {
    return true;
  }

  @Override
  public String getCatalog() throws SQLException {
    return null;
  }

  @Override
  public String getSchemaPattern() throws SQLException

  {
    return null;
  }

  @Override
  public String getTableNamePattern() throws SQLException

  {
    return null;
  }

  @Override
  public String[] getTableTypes() throws SQLException {
    return new String[] { "TABLE" };
  }
}