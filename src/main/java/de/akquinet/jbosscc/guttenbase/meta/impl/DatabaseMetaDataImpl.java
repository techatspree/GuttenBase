package de.akquinet.jbosscc.guttenbase.meta.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalDatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Information about a data base/schema.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DatabaseMetaDataImpl implements InternalDatabaseMetaData {
  private static final long serialVersionUID = 1L;

  private final String _schema;
  private final String _databaseName;
  private final int _majorVersion;
  private final int _minorVersion;
  private final DatabaseType _databaseType;
  private final Map<String, TableMetaData> _tableMetaDataMap = new LinkedHashMap<String, TableMetaData>();

  public DatabaseMetaDataImpl(final String schema, final String databaseName, final int majorVersion, final int minorVersion,
      final DatabaseType databaseType) {
    assert databaseName != null : "databaseName != null";
    assert schema != null : "schema != null";
    assert databaseType != null : "databaseType != null";

    _schema = schema;
    _databaseName = databaseName;
    _majorVersion = majorVersion;
    _minorVersion = minorVersion;
    _databaseType = databaseType;
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
  public String getDatabaseName() {
    return _databaseName;
  }

  @Override
  public int getMajorVersion() {
    return _majorVersion;
  }

  @Override
  public int getMinorVersion() {
    return _minorVersion;
  }

  @Override
  public List<TableMetaData> getTableMetaData() {
    return new ArrayList<TableMetaData>(_tableMetaDataMap.values());
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
    return getDatabaseName().toUpperCase().hashCode() + getSchema().toUpperCase().hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    final DatabaseMetaData that = (DatabaseMetaData) obj;

    return this.getDatabaseName().equalsIgnoreCase(that.getDatabaseName()) && this.getSchema().equalsIgnoreCase(that.getSchema());
  }
}
