package de.akquinet.jbosscc.guttenbase.export;

import de.akquinet.jbosscc.guttenbase.connector.GuttenBaseException;
import de.akquinet.jbosscc.guttenbase.exceptions.ExportException;
import org.apache.commons.io.input.ReaderInputStream;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

/**
 * Custom implementation of {@link PreparedStatement} dumping data to the given output stream. Only few inherited setter methods
 * have a meaningful implementation, most methods will throw a {@link UnsupportedOperationException}.
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ExportDumpPreparedStatement implements PreparedStatement {
  private final Exporter _exporter;

  public ExportDumpPreparedStatement(final Exporter exporter) {
    assert exporter != null : "exporter != null";

    _exporter = exporter;
  }

  @Override
  public boolean execute() {
    return true;
  }

  @Override
  public void setObject(final int parameterIndex, final Object x) {
    try {
      _exporter.writeObject(x);
    } catch (final Exception e) {
      throw new ExportException("setObject", e);
    }
  }

  @Override
  public void setBoolean(final int parameterIndex, final boolean x) {
    setObject(parameterIndex, x);
  }

  @Override
  public void setByte(final int parameterIndex, final byte x) {
    setObject(parameterIndex, x);
  }

  @Override
  public void setShort(final int parameterIndex, final short x) {
    setObject(parameterIndex, x);
  }

  @Override
  public void setInt(final int parameterIndex, final int x) {
    setObject(parameterIndex, x);
  }

  @Override
  public void setLong(final int parameterIndex, final long x) {
    setObject(parameterIndex, x);
  }

  @Override
  public void setFloat(final int parameterIndex, final float x) {
    setObject(parameterIndex, x);
  }

  @Override
  public void setDouble(final int parameterIndex, final double x) {
    setObject(parameterIndex, x);
  }

  @Override
  public void setBigDecimal(final int parameterIndex, final BigDecimal x) {
    setObject(parameterIndex, x);
  }

  @Override
  public void setString(final int parameterIndex, final String x) {
    setObject(parameterIndex, x);
  }

  @Override
  public void setBytes(final int parameterIndex, final byte[] x) {
    setObject(parameterIndex, x);
  }

  @Override
  public void setDate(final int parameterIndex, final Date x) {
    setObject(parameterIndex, x);
  }

  @Override
  public void setTime(final int parameterIndex, final Time x) {
    setObject(parameterIndex, x);
  }

  @Override
  public void setTimestamp(final int parameterIndex, final Timestamp x) {
    setObject(parameterIndex, x);
  }

  @Override
  public void setNull(final int parameterIndex, final int sqlType) {
    setObject(parameterIndex, null);
  }

  @Override
  public void setClob(final int parameterIndex, final Clob clob) throws SQLException {
    if (clob != null) {
      setObject(parameterIndex, new ExportDumpClob(clob.getAsciiStream()));
      flush();
      clob.free();
    } else {
      setObject(parameterIndex, null);
    }
  }

  @Override
  public void setClob(final int parameterIndex, final Reader reader) {
    assert reader != null : "reader != null";

    setObject(parameterIndex, new ExportDumpClob(new ReaderInputStream(reader)));
    flush();
  }

  @Override
  public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
    if (xmlObject != null) {
      setObject(parameterIndex, new ExportDumpSqlXML(xmlObject.getBinaryStream()));
      flush();
      xmlObject.free();
    } else {
      setObject(parameterIndex, null);
    }
  }

  @Override
  public void setBlob(final int parameterIndex, final InputStream inputStream) {
    assert inputStream != null : "inputStream != null";

    setObject(parameterIndex, new ExportDumpBlob(inputStream));

    flush();
  }

  @Override
  public void setBlob(final int parameterIndex, final Blob x) throws SQLException {
    if (x != null) {
      setBlob(parameterIndex, x.getBinaryStream());
      x.free();
    } else {
      setObject(parameterIndex, null);
    }
  }

  @Override
  public ResultSet executeQuery(final String sql) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int executeUpdate(final String sql) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void close() {
    // Ignored
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
    throw new UnsupportedOperationException();
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
    throw new UnsupportedOperationException();
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
  public int executeUpdate() {
    return 0;
  }

  @Override
  public ResultSet getResultSet() {
    throw new UnsupportedOperationException();
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
  public void setFetchSize(final int rows) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getFetchSize() {
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
    // Ignored
  }

  @Override
  public int[] executeBatch() {
    return new int[0];
  }

  @Override
  public Connection getConnection() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean getMoreResults(final int current) {
    return false;
  }

  @Override
  public ResultSet getGeneratedKeys() {
    throw new UnsupportedOperationException();
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
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isWrapperFor(final Class<?> iface) {
    return false;
  }

  @Override
  public ResultSet executeQuery() {
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
  public void addBatch() {
    // Ignored
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
  public void setArray(final int parameterIndex, final Array x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResultSetMetaData getMetaData() {
    throw new UnsupportedOperationException();
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
    setNull(parameterIndex, Types.JAVA_OBJECT);
  }

  @Override
  public void setURL(final int parameterIndex, final URL x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ParameterMetaData getParameterMetaData() {
    throw new UnsupportedOperationException();
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
  public void setNClob(final int parameterIndex, final Reader reader) {
    throw new UnsupportedOperationException();
  }

  private void flush() {
    try {
      _exporter.flush();
    } catch (final Exception e) {
      throw new GuttenBaseException("flush", e);
    }
  }

  // JRE 1.7

  public void closeOnCompletion() {
    throw new UnsupportedOperationException();
  }

  public boolean isCloseOnCompletion() {
    throw new UnsupportedOperationException();
  }
}
