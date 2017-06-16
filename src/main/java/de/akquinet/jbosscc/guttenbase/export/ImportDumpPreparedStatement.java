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
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ImportDumpPreparedStatement implements PreparedStatement
{
  private final Importer _importer;
  private final TableMetaData _tableMetaData;
  private final String _selectSql;
  private final DatabaseMetaData _databaseMetaData;

  public ImportDumpPreparedStatement(
      final Importer importer,
      final DatabaseMetaData databaseMetaData,
      final TableMetaData tableMetaData,
      final String selectSql)
  {
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
  public ResultSet executeQuery() throws SQLException
  {
    return executeQuery(_selectSql);
  }

  @Override
  public ResultSet executeQuery(final String sql) throws SQLException
  {
    assert sql != null : "sql != null";

    if (_tableMetaData.getFilteredRowCount() < 0)
    {
      throw new MissingDataException("Invalid number of expected rows");
    }

    return new ImportDumpResultSet(_importer, _databaseMetaData, _tableMetaData, Util.parseSelectedColumns(sql));
  }

  @Override
  public boolean execute()
  {
    return true;
  }

  @Override
  public void setFetchSize(final int expectedRows) throws SQLException
  {}

  @Override
  public void close() throws SQLException
  {}

  @Override
  public int getFetchSize() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBoolean(final int parameterIndex, final boolean x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setByte(final int parameterIndex, final byte x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setShort(final int parameterIndex, final short x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setInt(final int parameterIndex, final int x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setLong(final int parameterIndex, final long x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setFloat(final int parameterIndex, final float x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setDouble(final int parameterIndex, final double x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setString(final int parameterIndex, final String x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBytes(final int parameterIndex, final byte[] x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setDate(final int parameterIndex, final Date x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setTime(final int parameterIndex, final Time x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBlob(final int parameterIndex, final Blob x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int executeUpdate(final String sql) throws SQLException
  {
    throw new UnsupportedOperationException();
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
    return 2000;
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
    return null;
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
  public ResultSet getResultSet() throws SQLException
  {
    return null;
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
    throw new UnsupportedOperationException();
  }

  @Override
  public int[] executeBatch() throws SQLException
  {
    return null;
  }

  @Override
  public Connection getConnection() throws SQLException
  {
    return null;
  }

  @Override
  public boolean getMoreResults(final int current) throws SQLException
  {
    return false;
  }

  @Override
  public ResultSet getGeneratedKeys() throws SQLException
  {
    return null;
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
    return null;
  }

  @Override
  public boolean isWrapperFor(final Class<?> iface) throws SQLException
  {
    return false;
  }

  @Override
  public int executeUpdate() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNull(final int parameterIndex, final int sqlType) throws SQLException
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
  public void setObject(final int parameterIndex, final Object x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addBatch() throws SQLException
  {
    throw new UnsupportedOperationException();
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
  public void setClob(final int parameterIndex, final Clob x) throws SQLException
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
    return null;
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
    throw new UnsupportedOperationException();
  }

  @Override
  public void setURL(final int parameterIndex, final URL x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public ParameterMetaData getParameterMetaData() throws SQLException
  {
    return null;
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
  public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException
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
  public void setClob(final int parameterIndex, final Reader reader) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setNClob(final int parameterIndex, final Reader reader) throws SQLException
  {
    throw new UnsupportedOperationException();
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
