package de.akquinet.jbosscc.guttenbase.repository.impl;

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
import de.akquinet.jbosscc.guttenbase.tools.SelectWhereClause;
import de.akquinet.jbosscc.guttenbase.utils.Util;
import org.apache.log4j.Logger;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Get table meta data from connection.
 * <p/>
 * (C) 2012 by akquinet tech@spree
 *
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public class DatabaseMetaDataInspectorTool {
  private static final Logger LOG = Logger.getLogger(DatabaseMetaDataInspectorTool.class);

  private static final String TABLE_PLACEHOLDER = "<table>";
  private static final String SELECT_COUNT_STATEMENT = "SELECT COUNT(*) FROM " + TABLE_PLACEHOLDER;
  private static final String SELECT_NOTHING_STATEMENT = "SELECT * FROM " + TABLE_PLACEHOLDER + " WHERE 1 > 2";

  private final ConnectorRepository _connectorRepository;
  private final String _connectorId;

  public DatabaseMetaDataInspectorTool(final ConnectorRepository connectorRepository, final String connectorId) {
    assert connectorId != null : "connectorId != null";
    assert connectorRepository != null : "connectorRepository != null";

    _connectorRepository = connectorRepository;
    _connectorId = connectorId;
  }

  public DatabaseMetaData getDatabaseMetaData(final Connection connection) throws SQLException {
    LOG.info("Retrieving meta data for " + _connectorId);

    final ConnectorInfo connectionInfo = _connectorRepository.getConnectionInfo(_connectorId);
    final String schema = connectionInfo.getSchema();
    final String schemaPrefix = "".equals(Util.trim(schema)) ? "" : schema + ".";
    final java.sql.DatabaseMetaData metaData = connection.getMetaData();
    final Map<String, Object> properties = Arrays.stream(java.sql.DatabaseMetaData.class.getDeclaredMethods())
      .filter(method -> method.getParameterCount() == 0 && isPrimitive(method.getReturnType()))
      .map(method -> getValue(method, metaData)).filter(entry -> entry != null)
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    final DatabaseMetaDataImpl result = new DatabaseMetaDataImpl(schema, properties, connectionInfo.getDatabaseType());

    loadTables(result, metaData);

    updateTableMetaData(connection, metaData, result, schemaPrefix);

    LOG.info("Retrieving meta data for " + _connectorId + " DONE");

    return result;
  }


  private static Map.Entry<String, Object> getValue(final Method method, final java.sql.DatabaseMetaData data) {
    final String name = method.getName();

    try {
      final Object value = method.invoke(data);

      if (value != null) {
        return new Map.Entry<String, Object>() {
          @Override
          public String getKey() {
            return name;
          }

          @Override
          public Object getValue() {
            return value;
          }

          @Override
          public Object setValue(final Object value) {
            return value;
          }
        };
      }
    } catch (final Exception e) {
      LOG.warn("Could not get meta data property:" + name + "->" + e.getMessage());
    }

    return null;
  }

  private static boolean isPrimitive(final Class<?> clazz) {
    return clazz != Void.class && (clazz.isPrimitive() || clazz == String.class);
  }


  private void updateTableMetaData(final Connection connection, final java.sql.DatabaseMetaData metaData,
                                   final DatabaseMetaData databaseMetaData, final String schemaPrefix) throws SQLException {

    try (Statement statement = connection.createStatement()) {
      for (final TableMetaData table : databaseMetaData.getTableMetaData()) {
        final InternalTableMetaData tableMetaData = (InternalTableMetaData) table;
        updateTableWithRowCount(statement, tableMetaData, schemaPrefix);

        updateTableMetaDataWithColumnInformation(statement, tableMetaData, schemaPrefix);
      }
    }

    try {
      for (final TableMetaData table : databaseMetaData.getTableMetaData()) {
        final TableMetaDataImpl tableMetaData = (TableMetaDataImpl) table;

        updateColumnsWithPrimaryKeyInformation(metaData, databaseMetaData, tableMetaData);
        updateColumnsWithForeignKeyInformation(metaData, databaseMetaData, tableMetaData);
        updateTableWithIndexInformation(metaData, databaseMetaData, tableMetaData);
      }
    } catch (final Exception e) {
      // Some drivers such as JdbcOdbcBridge do not support this
      LOG.warn("Could not update additional schema information", e);
    }
  }

  private void updateColumnsWithForeignKeyInformation(final java.sql.DatabaseMetaData metaData, final DatabaseMetaData databaseMetaData,
                                                      final TableMetaData table) throws SQLException {
    LOG.debug("Retrieving foreign key information for " + table.getTableName());
    final DatabaseTableFilter tableFilter = _connectorRepository.getConnectorHint(_connectorId, DatabaseTableFilter.class).getValue();
    final ResultSet resultSet = metaData.getExportedKeys(tableFilter.getCatalog(databaseMetaData), tableFilter.getSchemaPattern(databaseMetaData), table.getTableName());

    while (resultSet.next()) {
      final String pkTableName = resultSet.getString("PKTABLE_NAME");
      final String pkColumnName = resultSet.getString("PKCOLUMN_NAME");
      final String fkTableName = resultSet.getString("FKTABLE_NAME");
      final String fkColumnName = resultSet.getString("FKCOLUMN_NAME");
      final String fkName = resultSet.getString("FK_NAME");

      final InternalTableMetaData pkTableMetaData = (InternalTableMetaData) databaseMetaData.getTableMetaData(pkTableName);
      final InternalTableMetaData fkTableMetaData = (InternalTableMetaData) databaseMetaData.getTableMetaData(fkTableName);

      if (fkTableMetaData == null || pkTableMetaData == null) {
        // this table might have been excluded from the list of tables handled by this batch
        LOG.warn("Unable to retrieve metadata information for table " + fkTableName + " referenced by " + pkTableName);
      } else {
        final ColumnMetaData pkColumn = pkTableMetaData.getColumnMetaData(pkColumnName);
        final ColumnMetaData fkColumn = fkTableMetaData.getColumnMetaData(fkColumnName);

        pkTableMetaData.addExportedForeignKey(new ForeignKeyMetaDataImpl(pkTableMetaData, fkName, fkColumn, pkColumn));
        fkTableMetaData.addImportedForeignKey(new ForeignKeyMetaDataImpl(fkTableMetaData, fkName, fkColumn, pkColumn));
      }
    }

    resultSet.close();
  }

  private void updateTableWithIndexInformation(final java.sql.DatabaseMetaData metaData, final DatabaseMetaData databaseMetaData,
                                               final InternalTableMetaData table) throws SQLException {
    LOG.debug("Retrieving index information for " + table.getTableName());

    final DatabaseTableFilter tableFilter = _connectorRepository.getConnectorHint(_connectorId, DatabaseTableFilter.class).getValue();
    final ResultSet resultSet = metaData.getIndexInfo(tableFilter.getCatalog(databaseMetaData), tableFilter.getSchema(databaseMetaData), table.getTableName(), false,
      true);

    while (resultSet.next()) {
      final boolean nonUnique = resultSet.getBoolean("NON_UNIQUE");
      final String indexName = resultSet.getString("INDEX_NAME");
      final String columnName = resultSet.getString("COLUMN_NAME");
      final String ascOrDesc = resultSet.getString("ASC_OR_DESC");

      if (columnName != null) {
        final ColumnMetaData column = table.getColumnMetaData(columnName);

        // May be strange SYS...$ column as with Oracle
        if (column != null) {
          InternalIndexMetaData indexMetaData = (InternalIndexMetaData) table.getIndexMetaData(indexName);

          if (indexMetaData == null) {
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

  private void updateColumnsWithPrimaryKeyInformation(final java.sql.DatabaseMetaData metaData, final DatabaseMetaData databaseMetaData,
                                                      final TableMetaData table) throws SQLException {
    LOG.debug("Retrieving primary key information for " + table.getTableName());

    final DatabaseTableFilter tableFilter = _connectorRepository.getConnectorHint(_connectorId, DatabaseTableFilter.class).getValue();
    final ResultSet resultSet = metaData.getPrimaryKeys(tableFilter.getCatalog(databaseMetaData), tableFilter.getSchema(databaseMetaData), table.getTableName());

    while (resultSet.next()) {
      final String pkName = resultSet.getString("PK_NAME");
      final String columnName = resultSet.getString("COLUMN_NAME");

      if (pkName != null) {
        final InternalColumnMetaData columnMetaData = (InternalColumnMetaData) table.getColumnMetaData(columnName);

        if (columnMetaData == null) {
          throw new IllegalStateException("No column meta data for " + columnName);
        }

        columnMetaData.setPrimaryKey(true);
      }
    }

    resultSet.close();
  }

  private void updateTableMetaDataWithColumnInformation(final Statement statement, final InternalTableMetaData tableMetaData,
                                                        final String schemaPrefix) throws SQLException {
    final String tableName = escapeTableName(tableMetaData, schemaPrefix);
    final DatabaseColumnFilter columnFilter = _connectorRepository.getConnectorHint(_connectorId, DatabaseColumnFilter.class).getValue();
    LOG.debug("Retrieving column information for " + tableName);

    final String selectSQL = SELECT_NOTHING_STATEMENT.replace(TABLE_PLACEHOLDER, tableName);
    final ResultSet resultSet = statement.executeQuery(selectSQL);
    final ResultSetMetaData meta = resultSet.getMetaData();
    final int columnCount = meta.getColumnCount();

    for (int i = 1; i <= columnCount; i++) {
      final String columnTypeName = meta.getColumnTypeName(i);
      final int columnType = meta.getColumnType(i);
      final String columnName = meta.getColumnName(i);
      final String columnClassName = meta.getColumnClassName(i);
      final boolean isNullable = meta.isNullable(i) != ResultSetMetaData.columnNoNulls;
      final boolean isAutoIncrement = meta.isAutoIncrement(i);
      final int precision = meta.getPrecision(i);
      final int scale = meta.getScale(i);

      final ColumnMetaDataImpl column = new ColumnMetaDataImpl(columnType, columnName, columnTypeName, columnClassName, isNullable,
        isAutoIncrement, precision, scale, tableMetaData);

      if (columnFilter.accept(column)) {
        tableMetaData.addColumn(column);
      }
    }

    resultSet.close();
  }

  private String createWhereClause(final TableMetaData tableMetaData) throws SQLException {
    final SelectWhereClause selectWhereClause = _connectorRepository.getConnectorHint(_connectorId, SelectWhereClause.class).getValue();
    return selectWhereClause.getWhereClause(tableMetaData);
  }

  private void updateTableWithRowCount(final Statement statement, final InternalTableMetaData tableMetaData, final String schemaPrefix)
    throws SQLException {
    final String tableName = escapeTableName(tableMetaData, schemaPrefix);

    LOG.debug("Retrieving row count for " + tableName);

    final String countAllSQL = SELECT_COUNT_STATEMENT.replace(TABLE_PLACEHOLDER, tableName);
    final String filterClause = createWhereClause(tableMetaData).trim();
    final String countFilteredSQL = SELECT_COUNT_STATEMENT.replace(TABLE_PLACEHOLDER, tableName) + " " + filterClause;
    final int totalCount = getCount(statement, countAllSQL);
    final int filteredCount = "".equals(filterClause) ? totalCount : getCount(statement, countFilteredSQL);

    tableMetaData.setTotalRowCount(totalCount);
    tableMetaData.setFilteredRowCount(filteredCount);
  }

  private int getCount(final Statement statement, final String countAllSQL) throws SQLException {
    final ResultSet countResultSet = statement.executeQuery(countAllSQL);
    countResultSet.next();
    final int totalCount = countResultSet.getInt(1);
    countResultSet.close();
    return totalCount;
  }

  private void loadTables(final InternalDatabaseMetaData databaseMetaData, final java.sql.DatabaseMetaData metaData) throws SQLException {
    LOG.debug("Searching tables in schema " + databaseMetaData.getSchema());

    final DatabaseTableFilter tableFilter = _connectorRepository.getConnectorHint(_connectorId, DatabaseTableFilter.class).getValue();
    final ResultSet rs = metaData.getTables(tableFilter.getCatalog(databaseMetaData), tableFilter.getSchemaPattern(databaseMetaData),
      tableFilter.getTableNamePattern(databaseMetaData), tableFilter.getTableTypes(databaseMetaData));

    while (rs.next()) {
      final String tableCatalog = rs.getString("TABLE_CAT");
      final String tableSchema = rs.getString("TABLE_SCHEM");
      final String tableName = rs.getString("TABLE_NAME");
      final String tableType = rs.getString("TABLE_TYPE");

      LOG.debug("Found: " + tableCatalog + "/" + tableSchema + "/" + tableName + "/" + tableType);
      final InternalTableMetaData tableMetaData = new TableMetaDataImpl(tableName, databaseMetaData, tableType);

      if (tableFilter.accept(tableMetaData)) {
        databaseMetaData.addTableMetaData(tableMetaData);
      }
    }

    LOG.info("Filtered tables: " + databaseMetaData.getTableMetaData());
  }

  private static String escapeTableName(final InternalTableMetaData tableMetaData, final String schemaPrefix) {
    String tableName = schemaPrefix + tableMetaData.getTableName();

    if (tableName.contains(" ")) {
      tableName = "\"" + tableName + "\"";
    }

    return tableName;
  }
}
