package de.akquinet.jbosscc.guttenbase.export;

import de.akquinet.jbosscc.guttenbase.exceptions.ImportException;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * Special "{@link Connection}" that supports reading data from a stream.
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ImportDumpConnection implements Connection {
  private final Importer _importer;
  private boolean _closed;
  private final Set<TableMetaData> _importedTables = new HashSet<>();
  private TableMetaData _currentTableMetaData;
  private final DatabaseMetaData _databaseMetaData;

  public ImportDumpConnection(final Importer importer, final DatabaseMetaData databaseMetaData) {
    _databaseMetaData = databaseMetaData;
    assert importer != null : "importer != null";

    _importer = importer;
  }

  public void initializeReadTable(final TableMetaData table) {
    _currentTableMetaData = table;
  }

  public void finalizeReadTable(final TableMetaData table) {
    _currentTableMetaData = null;
  }

  /**
   * Returns custom PreparedStatement statement. {@inheritDoc}
   */
  @Override
  public PreparedStatement prepareStatement(final String sql) {
    // Read header only once
    if (_importedTables.add(_currentTableMetaData)) {
      seekTableHeader(_currentTableMetaData);
    }

    return new ImportDumpPreparedStatement(_importer, _databaseMetaData, _currentTableMetaData, sql);
  }

  private void seekTableHeader(final TableMetaData tableMetaData) throws ImportException {
    try {
      _importer.seekTableHeader(tableMetaData);
    } catch (final Exception e) {
      throw new ImportException("getObject", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() {
    try {
      _importer.finishImport();
    } catch (final Exception e) {
      throw new ImportException("close", e);
    } finally {
      _closed = true;
      _importedTables.clear();
    }
  }

  @Override
  public boolean isClosed() {
    return _closed;
  }

  @Override
  public void commit() {
  }

  @Override
  public void setAutoCommit(final boolean autoCommit) {
  }

  @Override
  public boolean getAutoCommit() {
    return false;
  }

  @Override
  public void rollback() {
  }

  @Override
  public void setReadOnly(final boolean readOnly) {
  }

  @Override
  public boolean isReadOnly() {
    return true;
  }

  @Override
  public Statement createStatement() {
    return prepareStatement("");
  }

  @Override
  public <T> T unwrap(final Class<T> iface) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isWrapperFor(final Class<?> iface) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CallableStatement prepareCall(final String sql) {

    throw new UnsupportedOperationException();
  }

  @Override
  public String nativeSQL(final String sql) {
    throw new UnsupportedOperationException();
  }

  @Override
  public java.sql.DatabaseMetaData getMetaData() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setCatalog(final String catalog) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getCatalog() {

    throw new UnsupportedOperationException();
  }

  @Override
  public void setTransactionIsolation(final int level) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getTransactionIsolation() {

    return Connection.TRANSACTION_NONE;
  }

  @Override
  public SQLWarning getWarnings() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clearWarnings() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Statement createStatement(final int resultSetType, final int resultSetConcurrency) {
    return createStatement();
  }

  @Override
  public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency)
      {
    return prepareStatement(sql);
  }

  @Override
  public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency)
      {

    throw new UnsupportedOperationException();
  }

  @Override
  public Map<String, Class<?>> getTypeMap() {

    throw new UnsupportedOperationException();
  }

  @Override
  public void setTypeMap(final Map<String, Class<?>> map) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setHoldability(final int holdability) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getHoldability() {

    return 0;
  }

  @Override
  public Savepoint setSavepoint() {

    throw new UnsupportedOperationException();
  }

  @Override
  public Savepoint setSavepoint(final String name) {

    throw new UnsupportedOperationException();
  }

  @Override
  public void rollback(final Savepoint savepoint) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void releaseSavepoint(final Savepoint savepoint) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability)
      {

    throw new UnsupportedOperationException();
  }

  @Override
  public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency,
                                            final int resultSetHoldability) {

    throw new UnsupportedOperationException();
  }

  @Override
  public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency,
                                       final int resultSetHoldability) {

    throw new UnsupportedOperationException();
  }

  @Override
  public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) {

    throw new UnsupportedOperationException();
  }

  @Override
  public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) {

    throw new UnsupportedOperationException();
  }

  @Override
  public PreparedStatement prepareStatement(final String sql, final String[] columnNames) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Clob createClob() {

    throw new UnsupportedOperationException();
  }

  @Override
  public Blob createBlob() {

    throw new UnsupportedOperationException();
  }

  @Override
  public NClob createNClob() {

    throw new UnsupportedOperationException();
  }

  @Override
  public SQLXML createSQLXML() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isValid(final int timeout) {

    throw new UnsupportedOperationException();
  }

  @Override
  public void setClientInfo(final String name, final String value) throws SQLClientInfoException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setClientInfo(final Properties properties) throws SQLClientInfoException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getClientInfo(final String name) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Properties getClientInfo() {

    throw new UnsupportedOperationException();
  }

  @Override
  public Array createArrayOf(final String typeName, final Object[] elements) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Struct createStruct(final String typeName, final Object[] attributes) {
    throw new UnsupportedOperationException();
  }

  // JRE 1.7

  public void setSchema(final String schema) {
    throw new UnsupportedOperationException();
  }

  public String getSchema() {
    throw new UnsupportedOperationException();
  }

  public void abort(final Executor executor) {
    throw new UnsupportedOperationException();
  }

  public void setNetworkTimeout(final Executor executor, final int milliseconds) {
    throw new UnsupportedOperationException();
  }

  public int getNetworkTimeout() {
    throw new UnsupportedOperationException();
  }
}
