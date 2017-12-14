package de.akquinet.jbosscc.guttenbase.meta.impl;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalDatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Information about a data base/schema.
 * <p></p>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DatabaseMetaDataImpl implements InternalDatabaseMetaData {
  private static final long serialVersionUID = 1L;

  private final String _schema;
  private final Map<String, TableMetaData> _tableMetaDataMap = new LinkedHashMap<>();
  private final DatabaseType _databaseType;
  private transient java.sql.DatabaseMetaData _databaseMetaData;
  private final Map<String, Object> _databaseProperties;

  public DatabaseMetaDataImpl(final String schema, final Map<String, Object> databaseProperties,
                              final DatabaseType databaseType) {
    assert databaseProperties != null : "databaseProperties != null";
    assert schema != null : "schema != null";
    assert databaseType != null : "d != null";

    _databaseProperties = databaseProperties;
    _databaseType = databaseType;
    _schema = schema.trim();
  }

  @Override
  public java.sql.DatabaseMetaData getDatabaseMetaData() {
    if (_databaseMetaData == null) {
      _databaseMetaData = createMetaDataProxy(_databaseProperties);
    }

    return _databaseMetaData;
  }

  @Override
  public DatabaseType getDatabaseType() {
    return _databaseType;
  }

  @Override
  public String getSchema() {
    return _schema;
  }

  @Override
  public String getSchemaPrefix() {
    return !"".equals(getSchema()) ? getSchema() + "." : "";
  }


  @Override
  public List<TableMetaData> getTableMetaData() {
    return new ArrayList<>(_tableMetaDataMap.values());
  }

  @Override
  public TableMetaData getTableMetaData(final String tableName) {
    assert tableName != null : "tableName != null";
    return _tableMetaDataMap.get(tableName.toUpperCase());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addTableMetaData(final TableMetaData tableMetaData) {
    _tableMetaDataMap.put(tableMetaData.getTableName().toUpperCase(), tableMetaData);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeTableMetaData(final TableMetaData tableMetaData) {
    _tableMetaDataMap.remove(tableMetaData.getTableName().toUpperCase());
  }

  @Override
  public int hashCode() {
    return getDatabaseType().hashCode() + getSchema().toUpperCase().hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    final DatabaseMetaData that = (DatabaseMetaData) obj;

    return this.getDatabaseType().equals(that.getDatabaseType()) && this.getSchema().equalsIgnoreCase(that.getSchema());
  }

  private java.sql.DatabaseMetaData createMetaDataProxy(final Map<String, Object> properties) {
    return (java.sql.DatabaseMetaData) Proxy.newProxyInstance(getClass().getClassLoader(),
      new Class[]{java.sql.DatabaseMetaData.class},
      (proxy, method, args) -> properties.get(method.getName())
    );
  }
}
