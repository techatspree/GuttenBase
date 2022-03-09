package de.akquinet.jbosscc.guttenbase.export;

import de.akquinet.jbosscc.guttenbase.exceptions.MissingDataException;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.utils.Util;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

/**
 * Custom implementation of {@link PreparedStatement} reading data from the given input stream. This done via the custom
 * {@link ImportDumpResultSet} object return by {@link #executeQuery()}.
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ImportDumpPreparedStatement implements PreparedStatement {
  private final Importer _importer;
  private final TableMetaData _tableMetaData;
  private final String _selectSql;
  private final DatabaseMetaData _databaseMetaData;

  public ImportDumpPreparedStatement(
      final Importer importer,
      final DatabaseMetaData databaseMetaData,
      final TableMetaData tableMetaData,
      final String selectSql) {
    assert importer != null : "importer != null";
    assert databaseMetaData != null : "databaseMetaData != null";
    assert tableMetaData != null : "tableMetaData != null";
    assert selectSql != null : "selectSql != null";

    _importer = importer;
    _tableMetaData = tableMetaData;
    _selectSql = selectSql;
    _databaseMetaData = databaseMetaData;
  }

  @Override
  public ResultSet executeQuery() {
    return executeQuery(_selectSql);
  }

  @Override
  public ResultSet executeQuery(final String sql) {
    assert sql != null : "sql != null";

    if (_tableMetaData.getFilteredRowCount() < 0) {
      throw new MissingDataException("Invalid number of expected rows");
    }

    return new ImportDumpResultSet(_importer, _databaseMetaData, _tableMetaData, Util.parseSelectedColumns(sql));
  }

  @Override
  public boolean execute() {
    return true;
  }

  @Override
  public void setFetchSize(final int expectedRows) {
  }

  @Override
  public void close() {
  }

  @Override
  public int getFetchSize() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBoolean(final int parameterIndex, final boolean x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setByte(final int parameterIndex, final byte x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setShort(final int parameterIndex, final short x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setInt(final int parameterIndex, final int x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setLong(final int parameterIndex, final long x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setFloat(final int parameterIndex, final float x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setDouble(final int parameterIndex, final double x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBigDecimal(final int parameterIndex, final BigDecimal x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setString(final int parameterIndex, final String x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBytes(final int parameterIndex, final byte[] x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setDate(final int parameterIndex, final Date x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setTime(final int parameterIndex, final Time x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setTimestamp(final int parameterIndex, final Timestamp x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBlob(final int parameterIndex, final Blob x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int executeUpdate(final String sql) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getMaxFieldSize() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setMaxFieldSize(final int max) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getMaxRows() {
    return 2000;
  }

  @Override
  public void setMaxRows(final int max) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setEscapeProcessing(final boolean enable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getQueryTimeout() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setQueryTimeout(final int seconds) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void cancel() {
    throw new UnsupportedOperationException();
  }

  @Override
  public SQLWarning getWarnings() {
    return null;
  }

  @Override
  public void clearWarnings() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setCursorName(final String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean execute(final String sql) {
    return false;
  }

  @Override
  public ResultSet getResultSet() {
    return null;
  }

  @Override
  public int getUpdateCount() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean getMoreResults() {
    return false;
  }

  @Override
  public void setFetchDirection(final int direction) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getFetchDirection() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getResultSetConcurrency() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getResultSetType() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addBatch(final String sql) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clearBatch() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int[] executeBatch() {
    return null;
  }

  @Override
  public Connection getConnection() {
    return null;
  }

  @Override
  public boolean getMoreResults(final int current) {
    return false;
  }

  @Override
  public ResultSet getGeneratedKeys() {
    return null;
  }

  @Override
  public int executeUpdate(final String sql, final int autoGeneratedKeys) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int executeUpdate(final String sql, final int[] columnIndexes) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int executeUpdate(final String sql, final String[] columnNames) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean execute(final String sql, final int autoGeneratedKeys) {
    return false;
  }

  @Override
  public boolean execute(final String sql, final int[] columnIndexes) {
    return false;
  }

  @Override
  public boolean execute(final String sql, final String[] columnNames) {
    return false;
  }

  @Override
  public int getResultSetHoldability() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isClosed() {
    return false;
  }

  @Override
  public void setPoolable(final boolean poolable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isPoolable() {
    return false;
  }

  @Override
  public <T> T unwrap(final Class<T> iface) {
    return null;
  }

  @Override
  public boolean isWrapperFor(final Class<?> iface) {
    return false;
  }

  @Override
  public int executeUpdate() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNull(final int parameterIndex, final int sqlType) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clearParameters() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setObject(final int parameterIndex, final Object x, final int targetSqlType) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setObject(final int parameterIndex, final Object x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addBatch() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setCharacterStream(final int parameterIndex, final Reader reader, final int length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setRef(final int parameterIndex, final Ref x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setClob(final int parameterIndex, final Clob x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setArray(final int parameterIndex, final Array x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResultSetMetaData getMetaData() {
    return null;
  }

  @Override
  public void setDate(final int parameterIndex, final Date x, final Calendar cal) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setTime(final int parameterIndex, final Time x, final Calendar cal) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNull(final int parameterIndex, final int sqlType, final String typeName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setURL(final int parameterIndex, final URL x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ParameterMetaData getParameterMetaData() {
    return null;
  }

  @Override
  public void setRowId(final int parameterIndex, final RowId x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNString(final int parameterIndex, final String value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNClob(final int parameterIndex, final NClob value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setClob(final int parameterIndex, final Reader reader, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNClob(final int parameterIndex, final Reader reader, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength)
      {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setCharacterStream(final int parameterIndex, final Reader reader) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNCharacterStream(final int parameterIndex, final Reader value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setClob(final int parameterIndex, final Reader reader) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBlob(final int parameterIndex, final InputStream inputStream) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNClob(final int parameterIndex, final Reader reader) {
    throw new UnsupportedOperationException();
  }

  // JRE 1.7

  public void closeOnCompletion() {
    throw new UnsupportedOperationException();
  }

  public boolean isCloseOnCompletion() {
    throw new UnsupportedOperationException();
  }
}
