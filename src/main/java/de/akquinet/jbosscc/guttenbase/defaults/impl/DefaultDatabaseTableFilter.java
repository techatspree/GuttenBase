package de.akquinet.jbosscc.guttenbase.defaults.impl;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.DatabaseTableFilter;
import de.akquinet.jbosscc.guttenbase.utils.Util;

public class DefaultDatabaseTableFilter implements DatabaseTableFilter
{
  @Override
  public String getCatalog(final DatabaseMetaData databaseMetaData) throws SQLException
  {
    switch (databaseMetaData.getDatabaseType())
    {
      case MYSQL:
        return databaseMetaData.getSchema();
      default:
        return null;
    }
  }

  @Override
  public String getSchema(final DatabaseMetaData databaseMetaData) throws SQLException
  {
    return "".equals(Util.trim(databaseMetaData.getSchema())) ? null : databaseMetaData.getSchema();
  }

  @Override
  public String getSchemaPattern(final DatabaseMetaData databaseMetaData) throws SQLException

  {
    switch (databaseMetaData.getDatabaseType())
    {
      case MYSQL:
        return null;
      default:
        return getSchema(databaseMetaData);
    }
  }

  @Override
  public String getTableNamePattern(final DatabaseMetaData databaseMetaData) throws SQLException

  {
    return "%";
  }

  @Override
  public String[] getTableTypes(final DatabaseMetaData databaseMetaData) throws SQLException
  {
    return new String[]{"TABLE"};
  }

  @Override
  public boolean accept(final TableMetaData table) throws SQLException
  {
    return true;
  }
}
