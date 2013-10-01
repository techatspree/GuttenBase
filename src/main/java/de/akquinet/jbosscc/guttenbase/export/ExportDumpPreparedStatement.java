package de.akquinet.jbosscc.guttenbase.export;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;

import org.apache.commons.io.input.ReaderInputStream;

import de.akquinet.jbosscc.guttenbase.exceptions.ExportException;

/**
 * Custom implementation of {@link PreparedStatement} dumping data to the given output stream. Only few inherited setter methods
 * have a meaningful implementation, most methods will throw a {@link UnsupportedOperationException}.
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ExportDumpPreparedStatement implements PreparedStatement
{
  private final Exporter _exporter;

  public ExportDumpPreparedStatement(final Exporter exporter)
  {
    assert exporter != null : "exporter != null";

    _exporter = exporter;
  }

  @Override
  public boolean execute()
  {
    return true;
  }

  @Override
  public void setObject(final int parameterIndex, final Object x) throws SQLException
  {
    try
    {
      _exporter.writeObject(x);
    }
    catch (final Exception e)
    {
      throw new ExportException("setObject", e);
    }
  }

  @Override
  public void setBoolean(final int parameterIndex, final boolean x) throws SQLException
  {
    setObject(parameterIndex, x);
  }

  @Override
  public void setByte(final int parameterIndex, final byte x) throws SQLException
  {
    setObject(parameterIndex, x);
  }

  @Override
  public void setShort(final int parameterIndex, final short x) throws SQLException
  {
    setObject(parameterIndex, x);
  }

  @Override
  public void setInt(final int parameterIndex, final int x) throws SQLException
  {
    setObject(parameterIndex, x);
  }

  @Override
  public void setLong(final int parameterIndex, final long x) throws SQLException
  {
    setObject(parameterIndex, x);
  }

  @Override
  public void setFloat(final int parameterIndex, final float x) throws SQLException
  {
    setObject(parameterIndex, x);
  }

  @Override
  public void setDouble(final int parameterIndex, final double x) throws SQLException
  {
    setObject(parameterIndex, x);
  }

  @Override
  public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException
  {
    setObject(parameterIndex, x);
  }

  @Override
  public void setString(final int parameterIndex, final String x) throws SQLException
  {
    setObject(parameterIndex, x);
  }

  @Override
  public void setBytes(final int parameterIndex, final byte[] x) throws SQLException
  {
    setObject(parameterIndex, x);
  }

  @Override
  public void setDate(final int parameterIndex, final Date x) throws SQLException
  {
    setObject(parameterIndex, x);
  }

  @Override
  public void setTime(final int parameterIndex, final Time x) throws SQLException
  {
    setObject(parameterIndex, x);
  }

  @Override
  public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException
  {
    setObject(parameterIndex, x);
  }

  @Override
  public void setNull(final int parameterIndex, final int sqlType) throws SQLException
  {
    setObject(parameterIndex, null);
  }

  @Override
  public void setClob(final int parameterIndex, final Clob clob) throws SQLException
  {
    if (clob != null)
    {
      setObject(parameterIndex, new ExportDumpClob(clob.getAsciiStream()));
      flush();
      clob.free();
    }
    else
    {
      setObject(parameterIndex, null);
    }
  }

  @Override
  public void setClob(final int parameterIndex, final Reader reader) throws SQLException
  {
    assert reader != null : "reader != null";

    setObject(parameterIndex, new ExportDumpClob(new ReaderInputStream(reader)));
    flush();
  }

  @Override
  public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException
  {
    if (xmlObject != null)
    {
      setObject(parameterIndex, new ExportDumpSqlXML(xmlObject.getBinaryStream()));
      flush();
      xmlObject.free();
    }
    else
    {
      setObject(parameterIndex, null);
    }
  }

  @Override
  public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException
  {
    assert inputStream != null : "inputStream != null";

    setObject(parameterIndex, new ExportDumpBlob(inputStream));

    flush();
  }

  @Override
  public void setBlob(final int parameterIndex, final Blob x) throws SQLException
  {
    if (x != null)
    {
      setBlob(parameterIndex, x.getBinaryStream());
      x.free();
    }
    else
    {
      setObject(parameterIndex, null);
    }
  }

  @Override
  public ResultSet executeQuery(final String sql) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int executeUpdate(final String sql) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void close() throws SQLException
  {
    // Ignored
  }

  @Override
  public int getMaxFieldSize() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setMaxFieldSize(final int max) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getMaxRows() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setMaxRows(final int max) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setEscapeProcessing(final boolean enable) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getQueryTimeout() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setQueryTimeout(final int seconds) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void cancel() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public SQLWarning getWarnings() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clearWarnings() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setCursorName(final String name) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean execute(final String sql) throws SQLException
  {
    return false;
  }

  @Override
  public int executeUpdate() throws SQLException
  {
    return 0;
  }

  @Override
  public ResultSet getResultSet() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getUpdateCount() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean getMoreResults() throws SQLException
  {
    return false;
  }

  @Override
  public void setFetchDirection(final int direction) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getFetchDirection() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setFetchSize(final int rows) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getFetchSize() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getResultSetConcurrency() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getResultSetType() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addBatch(final String sql) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clearBatch() throws SQLException
  {
    // Ignored
  }

  @Override
  public int[] executeBatch() throws SQLException
  {
    return new int[0];
  }

  @Override
  public Connection getConnection() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean getMoreResults(final int current) throws SQLException
  {
    return false;
  }

  @Override
  public ResultSet getGeneratedKeys() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int executeUpdate(final String sql, final String[] columnNames) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException
  {
    return false;
  }

  @Override
  public boolean execute(final String sql, final int[] columnIndexes) throws SQLException
  {
    return false;
  }

  @Override
  public boolean execute(final String sql, final String[] columnNames) throws SQLException
  {
    return false;
  }

  @Override
  public int getResultSetHoldability() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isClosed() throws SQLException
  {
    return false;
  }

  @Override
  public void setPoolable(final boolean poolable) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isPoolable() throws SQLException
  {
    return false;
  }

  @Override
  public <T> T unwrap(final Class<T> iface) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isWrapperFor(final Class<?> iface) throws SQLException
  {
    return false;
  }

  @Override
  public ResultSet executeQuery() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clearParameters() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addBatch() throws SQLException
  {
    // Ignored
  }

  @Override
  public void setCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setRef(final int parameterIndex, final Ref x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setArray(final int parameterIndex, final Array x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException
  {
    setNull(parameterIndex, Types.JAVA_OBJECT);
  }

  @Override
  public void setURL(final int parameterIndex, final URL x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public ParameterMetaData getParameterMetaData() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setRowId(final int parameterIndex, final RowId x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNString(final int parameterIndex, final String value) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNClob(final int parameterIndex, final NClob value) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength)
      throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNClob(final int parameterIndex, final Reader reader) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  private void flush() throws SQLException
  {
    try
    {
      _exporter.flush();
    }
    catch (final Exception e)
    {
      throw new SQLException("flush", e);
    }
  }

  // JRE 1.7

  public void closeOnCompletion() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  public boolean isCloseOnCompletion() throws SQLException
  {
    throw new UnsupportedOperationException();
  }
}
