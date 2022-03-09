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
import java.sql.Date;
import java.sql.*;
import java.util.*;

/**
 * Special {@link ResultSet} that reads data from the given stream. Only few inherited getter methods have a meaningful
 * implementation, most methods will throw a {@link UnsupportedOperationException}.
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public class ImportDumpResultSet implements ResultSet {
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
      final List<String> selectedColumns) {
    assert importer != null : "objectInputStream != null";
    assert tableMetaData != null : "tableMetaData != null";

    _importer = importer;
    _tableMetaData = tableMetaData;
    _origTableMetaData = databaseMetaData.getTableMetaData(tableMetaData.getTableName());

    assert _origTableMetaData != null : "_origTableMetaData != null";

    buildColumnIndexMap(selectedColumns);
  }

  private void buildColumnIndexMap(final List<String> selectedColumns) {
    final List<ColumnMetaData> columnMetaData = _origTableMetaData.getColumnMetaData();

    // Use same ordering mechanism as defined by ColumnOrderHint
    // TODO: We cannot ask the connector repository for the right hint here!
    // Though it makes no sense, one could define another ColumnOrderHint for the
    // dump source connector, which will cause unpredictable results then
    columnMetaData.sort(new DefaultColumnComparator());

    for (int originalColumnIndex = 0; originalColumnIndex < columnMetaData.size(); originalColumnIndex++) {
      final String column = columnMetaData.get(originalColumnIndex).getColumnName().toUpperCase();
      final int columnIndex = selectedColumns.indexOf(column);

      if (columnIndex >= 0) {
        _columnIndexMap.put(columnIndex + 1, originalColumnIndex + 1);
      }
    }
  }

  @Override
  public boolean next() {
    _currentRow.clear();
    final boolean hasNext = _rowCount++ < _tableMetaData.getTotalRowCount();

    if (hasNext) // Prefetch current row
    {
      for (int i = 0; i < _origTableMetaData.getColumnCount(); i++) {
        _currentRow.add(readObject());
      }
    }

    return hasNext;
  }

  @Override
  public Object getObject(final int columnIndex) {
    final int realIndex = _columnIndexMap.get(columnIndex);
    final Object result = _currentRow.get(realIndex - 1);

    _wasNull = result == null;

    return result;
  }

  private Object readObject() {
    try {
      return _importer.readObject();
    } catch (final Exception e) {
      throw new ImportException("readObject", e);
    }
  }

  @Override
  public boolean getBoolean(final int columnIndex) {
    final Boolean object = (Boolean) getObject(columnIndex);
    return object != null && object;
  }

  @Override
  public byte getByte(final int columnIndex) {
    final Byte object = (Byte) getObject(columnIndex);

    return object == null ? 0 : object;
  }

  @Override
  public short getShort(final int columnIndex) {
    final Short object = (Short) getObject(columnIndex);
    return object == null ? 0 : object;
  }

  @Override
  public int getInt(final int columnIndex) {
    final Integer object = (Integer) getObject(columnIndex);
    return object == null ? 0 : object;
  }

  @Override
  public long getLong(final int columnIndex) {
    final Long object = (Long) getObject(columnIndex);
    return object == null ? 0 : object;
  }

  @Override
  public float getFloat(final int columnIndex) {
    final Float object = (Float) getObject(columnIndex);
    return object == null ? 0 : object;
  }

  @Override
  public double getDouble(final int columnIndex) {
    final Double object = (Double) getObject(columnIndex);
    return object == null ? 0 : object;
  }

  @Override
  public Blob getBlob(final int columnIndex) {
    return (Blob) getObject(columnIndex);
  }

  @Override
  public Clob getClob(final int columnIndex) {
    return (Clob) getObject(columnIndex);
  }

  @Override
  public SQLXML getSQLXML(final int columnIndex) {
    return (SQLXML) getObject(columnIndex);
  }

  @Override
  public String getString(final int columnIndex) {
    return (String) getObject(columnIndex);
  }

  @Override
  public BigDecimal getBigDecimal(final int columnIndex, final int scale) {
    return getBigDecimal(columnIndex);
  }

  @Override
  public BigDecimal getBigDecimal(final int columnIndex) {
    return (BigDecimal) getObject(columnIndex);
  }

  @Override
  public byte[] getBytes(final int columnIndex) {
    return (byte[]) getObject(columnIndex);
  }

  @Override
  public Date getDate(final int columnIndex) {
    return (Date) getObject(columnIndex);
  }

  @Override
  public Time getTime(final int columnIndex) {
    return (Time) getObject(columnIndex);
  }

  @Override
  public Timestamp getTimestamp(final int columnIndex) {
    return (Timestamp) getObject(columnIndex);
  }

  @Override
  public boolean wasNull() {
    return _wasNull;
  }

  @Override
  public void close() {
    _currentRow.clear();
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
  public InputStream getAsciiStream(final int columnIndex) {
    throw new UnsupportedOperationException();
  }

  @Override
  public InputStream getUnicodeStream(final int columnIndex) {
    throw new UnsupportedOperationException();
  }

  @Override
  public InputStream getBinaryStream(final int columnIndex) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getString(final String columnLabel) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean getBoolean(final String columnLabel) {
    throw new UnsupportedOperationException();
  }

  @Override
  public byte getByte(final String columnLabel) {
    throw new UnsupportedOperationException();
  }

  @Override
  public short getShort(final String columnLabel) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getInt(final String columnLabel) {
    throw new UnsupportedOperationException();
  }

  @Override
  public long getLong(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public float getFloat(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public double getDouble(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public BigDecimal getBigDecimal(final String columnLabel, final int scale) {

    throw new UnsupportedOperationException();
  }

  @Override
  public byte[] getBytes(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Date getDate(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Time getTime(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Timestamp getTimestamp(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public InputStream getAsciiStream(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public InputStream getUnicodeStream(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public InputStream getBinaryStream(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public SQLWarning getWarnings() {

    throw new UnsupportedOperationException();
  }

  @Override
  public void clearWarnings() {
    // Ignored
  }

  @Override
  public String getCursorName() {

    throw new UnsupportedOperationException();
  }

  @Override
  public ResultSetMetaData getMetaData() {

    throw new UnsupportedOperationException();
  }

  @Override
  public Object getObject(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public int findColumn(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Reader getCharacterStream(final int columnIndex) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Reader getCharacterStream(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public BigDecimal getBigDecimal(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isBeforeFirst() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isAfterLast() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isFirst() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isLast() {

    throw new UnsupportedOperationException();
  }

  @Override
  public void beforeFirst() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void afterLast() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean first() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean last() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getRow() {
    return _rowCount + 1;
  }

  @Override
  public boolean absolute(final int row) {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean relative(final int rows) {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean previous() {

    throw new UnsupportedOperationException();
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
  public int getType() {

    throw new UnsupportedOperationException();
  }

  @Override
  public int getConcurrency() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean rowUpdated() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean rowInserted() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean rowDeleted() {

    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNull(final int columnIndex) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBoolean(final int columnIndex, final boolean x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateByte(final int columnIndex, final byte x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateShort(final int columnIndex, final short x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateInt(final int columnIndex, final int x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateLong(final int columnIndex, final long x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateFloat(final int columnIndex, final float x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateDouble(final int columnIndex, final double x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBigDecimal(final int columnIndex, final BigDecimal x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateString(final int columnIndex, final String x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBytes(final int columnIndex, final byte[] x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateDate(final int columnIndex, final Date x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateTime(final int columnIndex, final Time x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateTimestamp(final int columnIndex, final Timestamp x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateAsciiStream(final int columnIndex, final InputStream x, final int length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBinaryStream(final int columnIndex, final InputStream x, final int length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateCharacterStream(final int columnIndex, final Reader x, final int length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateObject(final int columnIndex, final Object x, final int scaleOrLength) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateObject(final int columnIndex, final Object x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNull(final String columnLabel) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBoolean(final String columnLabel, final boolean x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateByte(final String columnLabel, final byte x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateShort(final String columnLabel, final short x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateInt(final String columnLabel, final int x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateLong(final String columnLabel, final long x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateFloat(final String columnLabel, final float x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateDouble(final String columnLabel, final double x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBigDecimal(final String columnLabel, final BigDecimal x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateString(final String columnLabel, final String x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBytes(final String columnLabel, final byte[] x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateDate(final String columnLabel, final Date x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateTime(final String columnLabel, final Time x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateTimestamp(final String columnLabel, final Timestamp x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateAsciiStream(final String columnLabel, final InputStream x, final int length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBinaryStream(final String columnLabel, final InputStream x, final int length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateCharacterStream(final String columnLabel, final Reader reader, final int length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateObject(final String columnLabel, final Object x, final int scaleOrLength) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateObject(final String columnLabel, final Object x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void insertRow() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateRow() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteRow() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void refreshRow() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void cancelRowUpdates() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void moveToInsertRow() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void moveToCurrentRow() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Statement getStatement() {

    throw new UnsupportedOperationException();
  }

  @Override
  public Object getObject(final int columnIndex, final Map<String, Class<?>> map) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Ref getRef(final int columnIndex) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Array getArray(final int columnIndex) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Object getObject(final String columnLabel, final Map<String, Class<?>> map) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Ref getRef(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Blob getBlob(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Clob getClob(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Array getArray(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Date getDate(final int columnIndex, final Calendar cal) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Date getDate(final String columnLabel, final Calendar cal) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Time getTime(final int columnIndex, final Calendar cal) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Time getTime(final String columnLabel, final Calendar cal) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Timestamp getTimestamp(final int columnIndex, final Calendar cal) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Timestamp getTimestamp(final String columnLabel, final Calendar cal) {

    throw new UnsupportedOperationException();
  }

  @Override
  public URL getURL(final int columnIndex) {

    throw new UnsupportedOperationException();
  }

  @Override
  public URL getURL(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public void updateRef(final int columnIndex, final Ref x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateRef(final String columnLabel, final Ref x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBlob(final int columnIndex, final Blob x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBlob(final String columnLabel, final Blob x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateClob(final int columnIndex, final Clob x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateClob(final String columnLabel, final Clob x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateArray(final int columnIndex, final Array x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateArray(final String columnLabel, final Array x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public RowId getRowId(final int columnIndex) {

    throw new UnsupportedOperationException();
  }

  @Override
  public RowId getRowId(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public void updateRowId(final int columnIndex, final RowId x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateRowId(final String columnLabel, final RowId x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getHoldability() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isClosed() {

    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNString(final int columnIndex, final String nString) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNString(final String columnLabel, final String nString) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNClob(final int columnIndex, final NClob nClob) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNClob(final String columnLabel, final NClob nClob) {
    throw new UnsupportedOperationException();
  }

  @Override
  public NClob getNClob(final int columnIndex) {

    throw new UnsupportedOperationException();
  }

  @Override
  public NClob getNClob(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public SQLXML getSQLXML(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public void updateSQLXML(final int columnIndex, final SQLXML xmlObject) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateSQLXML(final String columnLabel, final SQLXML xmlObject) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getNString(final int columnIndex) {

    throw new UnsupportedOperationException();
  }

  @Override
  public String getNString(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Reader getNCharacterStream(final int columnIndex) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Reader getNCharacterStream(final String columnLabel) {

    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNCharacterStream(final int columnIndex, final Reader x, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNCharacterStream(final String columnLabel, final Reader reader, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateAsciiStream(final int columnIndex, final InputStream x, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBinaryStream(final int columnIndex, final InputStream x, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateCharacterStream(final int columnIndex, final Reader x, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateAsciiStream(final String columnLabel, final InputStream x, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBinaryStream(final String columnLabel, final InputStream x, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateCharacterStream(final String columnLabel, final Reader reader, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBlob(final int columnIndex, final InputStream inputStream, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBlob(final String columnLabel, final InputStream inputStream, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateClob(final int columnIndex, final Reader reader, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateClob(final String columnLabel, final Reader reader, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNClob(final int columnIndex, final Reader reader, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNClob(final String columnLabel, final Reader reader, final long length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNCharacterStream(final int columnIndex, final Reader x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNCharacterStream(final String columnLabel, final Reader reader) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateAsciiStream(final int columnIndex, final InputStream x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBinaryStream(final int columnIndex, final InputStream x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateCharacterStream(final int columnIndex, final Reader x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateAsciiStream(final String columnLabel, final InputStream x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBinaryStream(final String columnLabel, final InputStream x) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateCharacterStream(final String columnLabel, final Reader reader) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBlob(final int columnIndex, final InputStream inputStream) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateBlob(final String columnLabel, final InputStream inputStream) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateClob(final int columnIndex, final Reader reader) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateClob(final String columnLabel, final Reader reader) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNClob(final int columnIndex, final Reader reader) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void updateNClob(final String columnLabel, final Reader reader) {
    throw new UnsupportedOperationException();
  }

  // JRE 1.7
  public <T> T getObject(final int columnIndex, final Class<T> type) {
    return type.cast(getObject(columnIndex));
  }

  public <T> T getObject(final String columnLabel, final Class<T> type) {
    return type.cast(getObject(columnLabel));
  }
}
