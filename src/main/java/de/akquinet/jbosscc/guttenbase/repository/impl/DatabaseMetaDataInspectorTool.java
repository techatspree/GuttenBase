package de.akquinet.jbosscc.guttenbase.repository.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import de.akquinet.jbosscc.guttenbase.connector.ConnectorInfo;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalDatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalIndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalTableMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.meta.impl.ColumnMetaDataImpl;
import de.akquinet.jbosscc.guttenbase.meta.impl.DatabaseMetaDataImpl;
import de.akquinet.jbosscc.guttenbase.meta.impl.ForeignKeyMetaDataImpl;
import de.akquinet.jbosscc.guttenbase.meta.impl.IndexMetaDataImpl;
import de.akquinet.jbosscc.guttenbase.meta.impl.TableMetaDataImpl;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.DatabaseColumnFilter;
import de.akquinet.jbosscc.guttenbase.repository.DatabaseTableFilter;
import de.akquinet.jbosscc.guttenbase.utils.Util;

/**
 * Get table meta data from connection. (C) 2012 by akquinet tech@spree
 * 
 * @author M. Dahm
 */
public class DatabaseMetaDataInspectorTool
{
  private static final Logger LOG = Logger.getLogger(DatabaseMetaDataInspectorTool.class);

  public static final String ERROR = "Error while checking if table exists: ";
  public static final String TABLE_PLACEHOLDER = "<table>";
  public static final String SELECT_COUNT_STATEMENT = "SELECT COUNT(*) FROM " + TABLE_PLACEHOLDER;
  public static final String SELECT_STATEMENT = "SELECT * FROM " + TABLE_PLACEHOLDER + " WHERE 1 > 2";
  public static final String NO_RESULT = "No result returned";

  private final ConnectorRepository _connectorRepository;
  private final String _connectorId;

  public DatabaseMetaDataInspectorTool(final ConnectorRepository connectorRepository, final String connectorId)
  {
    assert connectorId != null : "connectorId != null";
    assert connectorRepository != null : "connectorRepository != null";

    _connectorRepository = connectorRepository;
    _connectorId = connectorId;
  }

  public DatabaseMetaData getDatabaseMetaData(final Connection connection) throws SQLException
  {
    LOG.info("Retrieving meta data for " + _connectorId);

    final ConnectorInfo connectionInfo = _connectorRepository.getConnectionInfo(_connectorId);
    final String schema = connectionInfo.getSchema();
    final String schemaPrefix = "".equals(Util.trim(schema)) ? "" : schema + ".";
    final java.sql.DatabaseMetaData metaData = connection.getMetaData();

    final DatabaseMetaDataImpl result = new DatabaseMetaDataImpl(schema, getProductName(metaData), getMajorVersion(metaData),
        getMinorVersion(metaData), connectionInfo.getDatabaseType());

    loadTables(result, metaData);

    updateTableMetaData(connection, metaData, result, schemaPrefix);

    LOG.info("Retrieving meta data for " + _connectorId + " DONE");

    return result;
  }

  private void updateTableMetaData(final Connection connection, final java.sql.DatabaseMetaData metaData,
      final DatabaseMetaData databaseMetaData, final String schemaPrefix) throws SQLException
  {
    final Statement statement = connection.createStatement();

    try
    {
      for (final TableMetaData table : databaseMetaData.getTableMetaData())
      {
        final InternalTableMetaData tableMetaData = (InternalTableMetaData) table;
        updateTableWithRowCount(statement, tableMetaData, schemaPrefix);

        updateTableMetaDataWithColumnInformation(statement, tableMetaData, schemaPrefix);
      }
    }
    finally
    {
      statement.close();
    }

    try
    {
      for (final TableMetaData table : databaseMetaData.getTableMetaData())
      {
        final TableMetaDataImpl tableMetaData = (TableMetaDataImpl) table;

        updateColumnsWithPrimaryKeyInformation(metaData, databaseMetaData, tableMetaData);
        updateColumnsWithForeignKeyInformation(metaData, databaseMetaData, tableMetaData);
        updateTableWithIndexInformation(metaData, databaseMetaData, tableMetaData);
      }
    }
    catch (final Exception e)
    {
      // Some drivers such as JdbcOdbcBridge do not support this
      LOG.warn("Could not update additional schema information", e);
    }
  }

  private void updateColumnsWithForeignKeyInformation(final java.sql.DatabaseMetaData metaData,
      final DatabaseMetaData databaseMetaData, final TableMetaData table) throws SQLException
  {
    LOG.debug("Retrieving foreign key information for " + table.getTableName());
    final DatabaseTableFilter tableFilter = _connectorRepository.getConnectorHint(_connectorId, DatabaseTableFilter.class)
        .getValue();
    final ResultSet resultSet = metaData.getExportedKeys(null, getSchemaPattern(databaseMetaData, tableFilter),
        table.getTableName());

    while (resultSet.next())
    {
      final String pkTableName = resultSet.getString("PKTABLE_NAME");
      final String pkColumnName = resultSet.getString("PKCOLUMN_NAME");
      final String fkTableName = resultSet.getString("FKTABLE_NAME");
      final String fkColumnName = resultSet.getString("FKCOLUMN_NAME");
      final String fkName = resultSet.getString("FK_NAME");

      final InternalTableMetaData pkTableMetaData = (InternalTableMetaData) databaseMetaData.getTableMetaData(pkTableName);
      final InternalTableMetaData fkTableMetaData = (InternalTableMetaData) databaseMetaData.getTableMetaData(fkTableName);
      final ColumnMetaData pkColumn = pkTableMetaData.getColumnMetaData(pkColumnName);
      final ColumnMetaData fkColumn = fkTableMetaData.getColumnMetaData(fkColumnName);

      pkTableMetaData.addExportedForeignKey(new ForeignKeyMetaDataImpl(pkTableMetaData, fkName, fkColumn, pkColumn));
      fkTableMetaData.addImportedForeignKey(new ForeignKeyMetaDataImpl(fkTableMetaData, fkName, fkColumn, pkColumn));
    }

    resultSet.close();
  }

  private void updateTableWithIndexInformation(final java.sql.DatabaseMetaData metaData, final DatabaseMetaData databaseMetaData,
      final InternalTableMetaData table) throws SQLException
  {
    LOG.debug("Retrieving index information for " + table.getTableName());

    final DatabaseTableFilter tableFilter = _connectorRepository.getConnectorHint(_connectorId, DatabaseTableFilter.class)
        .getValue();
    final ResultSet resultSet = metaData.getIndexInfo(null, getSchemaPattern(databaseMetaData, tableFilter),
        table.getTableName(), false, true);

    while (resultSet.next())
    {
      final boolean nonUnique = resultSet.getBoolean("NON_UNIQUE");
      final String indexName = resultSet.getString("INDEX_NAME");
      final String columnName = resultSet.getString("COLUMN_NAME");
      final String ascOrDesc = resultSet.getString("ASC_OR_DESC");

      if (columnName != null)
      {
        final ColumnMetaData column = table.getColumnMetaData(columnName);

        // May be strange SYS...$ column as with Oracle
        if (column != null)
        {
          InternalIndexMetaData indexMetaData = (InternalIndexMetaData) table.getIndexMetaData(indexName);

          if (indexMetaData == null)
          {
            final boolean ascending = ascOrDesc == null || "A".equals(ascOrDesc);
            final boolean unique = !nonUnique;
            indexMetaData = new IndexMetaDataImpl(table, indexName, ascending, unique, column.isPrimaryKey());
            table.addIndex(indexMetaData);
          }

          indexMetaData.addColumn(column);
        }
      }
    }

    resultSet.close();
  }

  private void updateColumnsWithPrimaryKeyInformation(final java.sql.DatabaseMetaData metaData,
      final DatabaseMetaData databaseMetaData, final TableMetaData table) throws SQLException
  {
    LOG.debug("Retrieving primary key information for " + table.getTableName());

    final DatabaseTableFilter tableFilter = _connectorRepository.getConnectorHint(_connectorId, DatabaseTableFilter.class)
        .getValue();
    final ResultSet resultSet = metaData.getPrimaryKeys(null, getSchemaPattern(databaseMetaData, tableFilter),
        table.getTableName());

    while (resultSet.next())
    {
      final String pkName = resultSet.getString("PK_NAME");
      final String columnName = resultSet.getString("COLUMN_NAME");

      if (pkName != null)
      {
        final InternalColumnMetaData columnMetaData = (InternalColumnMetaData) table.getColumnMetaData(columnName);

        if (columnMetaData == null)
        {
          throw new IllegalStateException("No column meta data for " + columnName);
        }

        columnMetaData.setPrimaryKey(true);
      }
    }

    resultSet.close();
  }

  private void updateTableMetaDataWithColumnInformation(final Statement statement, final InternalTableMetaData tableMetaData,
      final String schemaPrefix) throws SQLException
  {
    final String tableName = escapeTableName(tableMetaData, schemaPrefix);
    final DatabaseColumnFilter columnFilter = _connectorRepository.getConnectorHint(_connectorId, DatabaseColumnFilter.class)
        .getValue();
    LOG.debug("Retrieving column information for " + tableName);

    final String selectSQL = SELECT_STATEMENT.replace(TABLE_PLACEHOLDER, tableName);
    final ResultSet resultSet = statement.executeQuery(selectSQL);
    final ResultSetMetaData meta = resultSet.getMetaData();
    final int columnCount = meta.getColumnCount();

    for (int i = 1; i <= columnCount; i++)
    {
      final String columnTypeName = meta.getColumnTypeName(i);
      final int columnType = meta.getColumnType(i);
      final String columnName = meta.getColumnName(i);
      final String columnClassName = meta.getColumnClassName(i);
      final boolean isNullable = meta.isNullable(i) != ResultSetMetaData.columnNoNulls;
      final boolean isAutoIncrement = meta.isAutoIncrement(i);
      final int precision = meta.getPrecision(i);
      final int scale = meta.getScale(i);

      final ColumnMetaDataImpl column = new ColumnMetaDataImpl(columnType, columnName, columnTypeName, columnClassName,
          isNullable, isAutoIncrement, precision, scale, tableMetaData);

      if (columnFilter.accept(column))
      {
        tableMetaData.addColumn(column);
      }
    }

    resultSet.close();
  }

  private void updateTableWithRowCount(final Statement statement, final InternalTableMetaData tableMetaData,
      final String schemaPrefix) throws SQLException
  {
    final String tableName = escapeTableName(tableMetaData, schemaPrefix);

    LOG.debug("Retrieving row count for " + tableName);

    final String countSQL = SELECT_COUNT_STATEMENT.replace(TABLE_PLACEHOLDER, tableName);
    final ResultSet countResultSet = statement.executeQuery(countSQL);
    countResultSet.next();
    final int totalCount = countResultSet.getInt(1);
    countResultSet.close();
    tableMetaData.setRowCount(totalCount);
  }

  private void loadTables(final InternalDatabaseMetaData databaseMetaData, final java.sql.DatabaseMetaData metaData)
      throws SQLException
  {
    final DatabaseTableFilter tableFilter = _connectorRepository.getConnectorHint(_connectorId, DatabaseTableFilter.class)
        .getValue();
    LOG.debug("Searching tables in schema " + databaseMetaData.getSchema());
    final ResultSet rs = metaData.getTables(tableFilter.getCatalog(), getSchemaPattern(databaseMetaData, tableFilter),
        tableFilter.getTableNamePattern(), tableFilter.getTableTypes());

    while (rs.next())
    {
      final String tableName = rs.getString("TABLE_NAME");
      final InternalTableMetaData tableMetaData = new TableMetaDataImpl(tableName, databaseMetaData);

      if (tableFilter.accept(tableMetaData))
      {
        databaseMetaData.addTableMetaData(tableMetaData);
      }
    }

    LOG.info("Found tables: " + databaseMetaData.getTableMetaData());
  }

  private static String getSchemaPattern(final DatabaseMetaData databaseMetaData, final DatabaseTableFilter tableFilter)
      throws SQLException
  {
    final String schemaPattern1 = tableFilter.getSchemaPattern();
    final String schemaPattern2 = "".equals(Util.trim(databaseMetaData.getSchema())) ? null : databaseMetaData.getSchema();

    if (schemaPattern1 != null)
    {
      return schemaPattern1;
    }
    else
    {
      return schemaPattern2;
    }

  }

  // Some drivers such as JdbcOdbcBridge do not support this
  private static String getProductName(final java.sql.DatabaseMetaData metaData)
  {
    try
    {
      return metaData.getDatabaseProductName();
    }
    catch (final Exception e)
    {
      LOG.warn("Could not get product name:" + e.getMessage());

      return "Unknown";
    }
  }

  private static int getMinorVersion(final java.sql.DatabaseMetaData metaData) throws SQLException
  {
    try
    {
      return metaData.getDatabaseMinorVersion();
    }
    catch (final Exception e)
    {
      LOG.warn("Could not get minor version:" + e.getMessage());
      return 0;
    }
  }

  private static int getMajorVersion(final java.sql.DatabaseMetaData metaData) throws SQLException
  {
    try
    {
      return metaData.getDatabaseMajorVersion();
    }
    catch (final Exception e)
    {
      LOG.warn("Could not get major version:" + e.getMessage());
      return 0;
    }
  }

  private static String escapeTableName(final InternalTableMetaData tableMetaData, final String schemaPrefix)
  {
    String tableName = schemaPrefix + tableMetaData.getTableName();

    if (tableName.contains(" "))
    {
      tableName = "\"" + tableName + "\"";
    }

    return tableName;
  }
}
