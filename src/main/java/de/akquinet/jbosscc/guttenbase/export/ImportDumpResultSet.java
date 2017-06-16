package de.akquinet.jbosscc.guttenbase.export;

import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultColumnComparator;
import de.akquinet.jbosscc.guttenbase.exceptions.ImportException;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Special {@link ResultSet} that reads data from the given stream. Only few inherited getter methods have a meaningful
 * implementation, most methods will throw a {@link UnsupportedOperationException}.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public class ImportDumpResultSet implements ResultSet
{
  private int _rowCount;
  private final Importer _importer;
  private boolean _wasNull;
  private final TableMetaData _tableMetaData;

  /**
   * Since _tableMetaData may contain a limited set of columns, but the dumped data contains all columns, we need to map the
   * indices.
   */
  private final Map<Integer, Integer> _columnIndexMap = new HashMap<>();
  private final List<Object> _currentRow = new ArrayList<>();
  private final TableMetaData _origTableMetaData;

  public ImportDumpResultSet(
      final Importer importer,
      final DatabaseMetaData databaseMetaData,
      final TableMetaData tableMetaData,
      final List<String> selectedColumns) throws SQLException
  {
    assert importer != null : "objectInputStream != null";
    assert tableMetaData != null : "tableMetaData != null";

    _importer = importer;
    _tableMetaData = tableMetaData;
    _origTableMetaData = databaseMetaData.getTableMetaData(tableMetaData.getTableName());

    assert _origTableMetaData != null : "_origTableMetaData != null";

    buildColumnIndexMap(selectedColumns);
  }

  private void buildColumnIndexMap(final List<String> selectedColumns) throws SQLException
  {
    final List<ColumnMetaData> columnMetaData = _origTableMetaData.getColumnMetaData();

    // Use same ordering mechanism as defined by ColumnOrderHint
    // TODO: We cannot ask the connector repository for the right hint here!
    // Though it makes no sense, one could define another ColumnOrderHint for the
    // dump source connector, which will cause unpredictable results then
    columnMetaData.sort(new DefaultColumnComparator());

    for (int originalColumnIndex = 0; originalColumnIndex < columnMetaData.size(); originalColumnIndex++)
    {
      final String column = columnMetaData.get(originalColumnIndex).getColumnName().toUpperCase();
      final int columnIndex = selectedColumns.indexOf(column);

      if (columnIndex >= 0)
      {
        _columnIndexMap.put(columnIndex + 1, originalColumnIndex + 1);
      }
    }
  }

  @Override
  public boolean next() throws SQLException
  {
    _currentRow.clear();
    final boolean hasNext = _rowCount++ < _tableMetaData.getTotalRowCount();

    if (hasNext) // Prefetch current row
    {
      for (int i = 0; i < _origTableMetaData.getColumnCount(); i++)
      {
        _currentRow.add(readObject());
      }
    }

    return hasNext;
  }

  @Override
  public Object getObject(final int columnIndex) throws SQLException
  {
    final int realIndex = _columnIndexMap.get(columnIndex);
    final Object result = _currentRow.get(realIndex - 1);

    _wasNull = result == null;

    return result;
  }

  private Object readObject() throws SQLException
  {
    try
    {
      return _importer.readObject();
    }
    catch (final Exception e)
    {
      throw new ImportException("readObject", e);
    }
  }

  @Override
  public boolean getBoolean(final int columnIndex) throws SQLException
  {
    final Boolean object = (Boolean) getObject(columnIndex);
    return object == null ? false : object;
  }

  @Override
  public byte getByte(final int columnIndex) throws SQLException
  {
    final Byte object = (Byte) getObject(columnIndex);

    return object == null ? 0 : object;
  }

  @Override
  public short getShort(final int columnIndex) throws SQLException
  {
    final Short object = (Short) getObject(columnIndex);
    return object == null ? 0 : object;
  }

  @Override
  public int getInt(final int columnIndex) throws SQLException
  {
    final Integer object = (Integer) getObject(columnIndex);
    return object == null ? 0 : object;
  }

  @Override
  public long getLong(final int columnIndex) throws SQLException
  {
    final Long object = (Long) getObject(columnIndex);
    return object == null ? 0 : object;
  }

  @Override
  public float getFloat(final int columnIndex) throws SQLException
  {
    final Float object = (Float) getObject(columnIndex);
    return object == null ? 0 : object;
  }

  @Override
  public double getDouble(final int columnIndex) throws SQLException
  {
    final Double object = (Double) getObject(columnIndex);
    return object == null ? 0 : object;
  }

  @Override
  public Blob getBlob(final int columnIndex) throws SQLException
  {
    return (Blob) getObject(columnIndex);
  }

  @Override
  public Clob getClob(final int columnIndex) throws SQLException
  {
    return (Clob) getObject(columnIndex);
  }

  @Override
  public SQLXML getSQLXML(final int columnIndex) throws SQLException
  {
    return (SQLXML) getObject(columnIndex);
  }

  @Override
  public String getString(final int columnIndex) throws SQLException
  {
    return (String) getObject(columnIndex);
  }

  @Override
  public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException
  {
    return getBigDecimal(columnIndex);
  }

  @Override
  public BigDecimal getBigDecimal(final int columnIndex) throws SQLException
  {
    return (BigDecimal) getObject(columnIndex);
  }

  @Override
  public byte[] getBytes(final int columnIndex) throws SQLException
  {
    return (byte[]) getObject(columnIndex);
  }

  @Override
  public Date getDate(final int columnIndex) throws SQLException
  {
    return (Date) getObject(columnIndex);
  }

  @Override
  public Time getTime(final int columnIndex) throws SQLException
  {
    return (Time) getObject(columnIndex);
  }

  @Override
  public Timestamp getTimestamp(final int columnIndex) throws SQLException
  {
    return (Timestamp) getObject(columnIndex);
  }

  @Override
  public boolean wasNull() throws SQLException
  {
    return _wasNull;
  }

  @Override
  public void close() throws SQLException
  {
    _currentRow.clear();
  }

  @Override
  public <T> T unwrap(final Class<T> iface) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isWrapperFor(final Class<?> iface) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public InputStream getAsciiStream(final int columnIndex) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public InputStream getUnicodeStream(final int columnIndex) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public InputStream getBinaryStream(final int columnIndex) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getString(final String columnLabel) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean getBoolean(final String columnLabel) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public byte getByte(final String columnLabel) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public short getShort(final String columnLabel) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getInt(final String columnLabel) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public long getLong(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public float getFloat(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public double getDouble(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public BigDecimal getBigDecimal(final String columnLabel, final int scale) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public byte[] getBytes(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Date getDate(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Time getTime(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Timestamp getTimestamp(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public InputStream getAsciiStream(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public InputStream getUnicodeStream(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public InputStream getBinaryStream(final String columnLabel) throws SQLException
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
    // Ignored
  }

  @Override
  public String getCursorName() throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Object getObject(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public int findColumn(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Reader getCharacterStream(final int columnIndex) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Reader getCharacterStream(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public BigDecimal getBigDecimal(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isBeforeFirst() throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isAfterLast() throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isFirst() throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isLast() throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public void beforeFirst() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void afterLast() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean first() throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean last() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getRow() throws SQLException
  {
    return _rowCount + 1;
  }

  @Override
  public boolean absolute(final int row) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean relative(final int rows) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean previous() throws SQLException
  {

    throw new UnsupportedOperationException();
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
  public int getType() throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public int getConcurrency() throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean rowUpdated() throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean rowInserted() throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean rowDeleted() throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNull(final int columnIndex) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBoolean(final int columnIndex, final boolean x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateByte(final int columnIndex, final byte x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateShort(final int columnIndex, final short x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateInt(final int columnIndex, final int x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateLong(final int columnIndex, final long x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateFloat(final int columnIndex, final float x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateDouble(final int columnIndex, final double x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBigDecimal(final int columnIndex, final BigDecimal x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateString(final int columnIndex, final String x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBytes(final int columnIndex, final byte[] x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateDate(final int columnIndex, final Date x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateTime(final int columnIndex, final Time x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateTimestamp(final int columnIndex, final Timestamp x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateAsciiStream(final int columnIndex, final InputStream x, final int length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBinaryStream(final int columnIndex, final InputStream x, final int length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateObject(final int columnIndex, final Object x, final int scaleOrLength) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateObject(final int columnIndex, final Object x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNull(final String columnLabel) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBoolean(final String columnLabel, final boolean x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateByte(final String columnLabel, final byte x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateShort(final String columnLabel, final short x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateInt(final String columnLabel, final int x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateLong(final String columnLabel, final long x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateFloat(final String columnLabel, final float x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateDouble(final String columnLabel, final double x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBigDecimal(final String columnLabel, final BigDecimal x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateString(final String columnLabel, final String x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBytes(final String columnLabel, final byte[] x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateDate(final String columnLabel, final Date x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateTime(final String columnLabel, final Time x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateTimestamp(final String columnLabel, final Timestamp x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateAsciiStream(final String columnLabel, final InputStream x, final int length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBinaryStream(final String columnLabel, final InputStream x, final int length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateCharacterStream(final String columnLabel, final Reader reader, final int length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateObject(final String columnLabel, final Object x, final int scaleOrLength) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateObject(final String columnLabel, final Object x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void insertRow() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateRow() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteRow() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void refreshRow() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void cancelRowUpdates() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void moveToInsertRow() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void moveToCurrentRow() throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public Statement getStatement() throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Object getObject(final int columnIndex, final Map<String, Class<?>> map) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Ref getRef(final int columnIndex) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Array getArray(final int columnIndex) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Object getObject(final String columnLabel, final Map<String, Class<?>> map) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Ref getRef(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Blob getBlob(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Clob getClob(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Array getArray(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Date getDate(final int columnIndex, final Calendar cal) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Date getDate(final String columnLabel, final Calendar cal) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Time getTime(final int columnIndex, final Calendar cal) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Time getTime(final String columnLabel, final Calendar cal) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Timestamp getTimestamp(final int columnIndex, final Calendar cal) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Timestamp getTimestamp(final String columnLabel, final Calendar cal) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public URL getURL(final int columnIndex) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public URL getURL(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public void updateRef(final int columnIndex, final Ref x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateRef(final String columnLabel, final Ref x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBlob(final int columnIndex, final Blob x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBlob(final String columnLabel, final Blob x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateClob(final int columnIndex, final Clob x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateClob(final String columnLabel, final Clob x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateArray(final int columnIndex, final Array x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateArray(final String columnLabel, final Array x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public RowId getRowId(final int columnIndex) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public RowId getRowId(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public void updateRowId(final int columnIndex, final RowId x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateRowId(final String columnLabel, final RowId x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getHoldability() throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isClosed() throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNString(final int columnIndex, final String nString) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNString(final String columnLabel, final String nString) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNClob(final int columnIndex, final NClob nClob) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNClob(final String columnLabel, final NClob nClob) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public NClob getNClob(final int columnIndex) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public NClob getNClob(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public SQLXML getSQLXML(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public void updateSQLXML(final int columnIndex, final SQLXML xmlObject) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateSQLXML(final String columnLabel, final SQLXML xmlObject) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getNString(final int columnIndex) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public String getNString(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Reader getNCharacterStream(final int columnIndex) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public Reader getNCharacterStream(final String columnLabel) throws SQLException
  {

    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNCharacterStream(final String columnLabel, final Reader reader, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateAsciiStream(final int columnIndex, final InputStream x, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBinaryStream(final int columnIndex, final InputStream x, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateAsciiStream(final String columnLabel, final InputStream x, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBinaryStream(final String columnLabel, final InputStream x, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateCharacterStream(final String columnLabel, final Reader reader, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBlob(final int columnIndex, final InputStream inputStream, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBlob(final String columnLabel, final InputStream inputStream, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateClob(final int columnIndex, final Reader reader, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateClob(final String columnLabel, final Reader reader, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNClob(final int columnIndex, final Reader reader, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNClob(final String columnLabel, final Reader reader, final long length) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNCharacterStream(final int columnIndex, final Reader x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNCharacterStream(final String columnLabel, final Reader reader) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateAsciiStream(final int columnIndex, final InputStream x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBinaryStream(final int columnIndex, final InputStream x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateCharacterStream(final int columnIndex, final Reader x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateAsciiStream(final String columnLabel, final InputStream x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBinaryStream(final String columnLabel, final InputStream x) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateCharacterStream(final String columnLabel, final Reader reader) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBlob(final int columnIndex, final InputStream inputStream) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBlob(final String columnLabel, final InputStream inputStream) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateClob(final int columnIndex, final Reader reader) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateClob(final String columnLabel, final Reader reader) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNClob(final int columnIndex, final Reader reader) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNClob(final String columnLabel, final Reader reader) throws SQLException
  {
    throw new UnsupportedOperationException();
  }

  // JRE 1.7
  public <T> T getObject(final int columnIndex, final Class<T> type) throws SQLException
  {
    return type.cast(getObject(columnIndex));
  }

  public <T> T getObject(final String columnLabel, final Class<T> type) throws SQLException
  {
    return type.cast(getObject(columnLabel));
  }
}
