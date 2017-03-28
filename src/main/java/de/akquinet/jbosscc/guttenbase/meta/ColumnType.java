package de.akquinet.jbosscc.guttenbase.meta;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.exceptions.UnhandledColumnTypeException;
import de.akquinet.jbosscc.guttenbase.utils.Util;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Define column type and mapping methods
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public enum ColumnType
{
  CLASS_UNKNOWN(Void.class), //
  CLASS_STRING(String.class), //
  CLASS_BIGDECIMAL(BigDecimal.class), //
  CLASS_BLOB(Blob.class), //
  CLASS_CLOB(Clob.class), //
  CLASS_SQLXML(SQLXML.class), //
  CLASS_OBJECT(Object.class, Serializable.class, Util.ByteArrayClass), //
  CLASS_DATE(java.sql.Date.class), //
  CLASS_TIMESTAMP(java.sql.Timestamp.class), //
  CLASS_TIME(java.sql.Time.class), //
  CLASS_INTEGER(Integer.class), //
  CLASS_BOOLEAN(Boolean.class), //
  CLASS_LONG(Long.class, BigInteger.class), //
  CLASS_DOUBLE(Double.class), //
  CLASS_FLOAT(Float.class), //
  CLASS_SHORT(Short.class);//

  private final List<Class<?>> _columnClasses;

  private static final Map<Class<?>, ColumnType> COLUMN_TYPES = new HashMap<>();

  ColumnType(final Class<?>... columnClasses)
  {
    _columnClasses = Arrays.asList(columnClasses);
  }

  /**
   * Get value from {@link ResultSet}
   */
  public Object getValue(final ResultSet resultSet, final int columnIndex) throws SQLException
  {
    final Object result = getValueFromResultset(resultSet, columnIndex);

    if (resultSet.wasNull())
    {
      return null;
    }
    else
    {
      return result;
    }
  }

  private Object getValueFromResultset(final ResultSet resultSet, final int columnIndex)
          throws SQLException
  {
    switch (this)
    {
      case CLASS_STRING:
        return resultSet.getString(columnIndex);
      case CLASS_DOUBLE:
        return resultSet.getDouble(columnIndex);
      case CLASS_INTEGER:
        return resultSet.getInt(columnIndex);
      case CLASS_LONG:
        return resultSet.getLong(columnIndex);
      case CLASS_BLOB:
        return resultSet.getBlob(columnIndex);
      case CLASS_CLOB:
        return resultSet.getClob(columnIndex);
      case CLASS_SQLXML:
        return resultSet.getSQLXML(columnIndex);
      case CLASS_FLOAT:
        return resultSet.getFloat(columnIndex);
      case CLASS_BOOLEAN:
        return resultSet.getBoolean(columnIndex);
      case CLASS_BIGDECIMAL:
        return resultSet.getBigDecimal(columnIndex);
      case CLASS_TIMESTAMP:
        return resultSet.getTimestamp(columnIndex);
      case CLASS_DATE:
        return resultSet.getDate(columnIndex);
      case CLASS_SHORT:
        return resultSet.getShort(columnIndex);
      case CLASS_TIME:
        return resultSet.getTime(columnIndex);
      case CLASS_OBJECT:
        return resultSet.getObject(columnIndex);
      default:
        throw new UnhandledColumnTypeException("Unhandled column type (" + this + ")");
    }
  }

  /**
   * Set value in {@link PreparedStatement}
   */
  public Closeable setValue(final PreparedStatement insertStatement, final int columnIndex, final Object data,
                            final DatabaseType databaseType, final int sqlType) throws SQLException
  {
    if (data == null)
    {
      insertStatement.setNull(columnIndex, sqlType);
      return null;
    }
    else
    {
      return setStatementValue(insertStatement, columnIndex, data, databaseType);
    }
  }

  private Closeable setStatementValue(final PreparedStatement insertStatement, final int columnIndex, final Object data,
                                      final DatabaseType databaseType) throws SQLException
  {
    Closeable result = null;

    switch (this)
    {
      case CLASS_STRING:
        insertStatement.setString(columnIndex, (String) data);
        break;
      case CLASS_INTEGER:
        insertStatement.setInt(columnIndex, (Integer) data);
        break;
      case CLASS_LONG:
        insertStatement.setLong(columnIndex, (Long) data);
        break;
      case CLASS_DOUBLE:
        insertStatement.setDouble(columnIndex, (Double) data);
        break;
      case CLASS_BLOB:
        if (driverSupportsStream(databaseType))
        {
          final InputStream inputStream = ((Blob) data).getBinaryStream();
          result = inputStream;
          insertStatement.setBlob(columnIndex, inputStream);
        }
        else
        {
          final Blob blob = (Blob) data;
          result = new ClosableBlobWrapper(blob);
          insertStatement.setBlob(columnIndex, blob);
        }
        break;
      case CLASS_CLOB:
        if (driverSupportsStream(databaseType))
        {
          final Reader characterStream = ((Clob) data).getCharacterStream();
          result = characterStream;
          insertStatement.setClob(columnIndex, characterStream);
        }
        else
        {
          final Clob clob = (Clob) data;
          result = new ClosableClobWrapper(clob);
          insertStatement.setClob(columnIndex, clob);
        }
        break;
      case CLASS_SQLXML:
        if (driverSupportsStream(databaseType))
        {
          final InputStream inputStream = ((SQLXML) data).getBinaryStream();
          result = inputStream;
          insertStatement.setBlob(columnIndex, inputStream);
        }
        else
        {
          final SQLXML blob = (SQLXML) data;
          result = new ClosableSqlXmlWrapper(blob);
          insertStatement.setSQLXML(columnIndex, blob);
        }
        break;
      case CLASS_BOOLEAN:
        insertStatement.setBoolean(columnIndex, (Boolean) data);
        break;
      case CLASS_BIGDECIMAL:
        insertStatement.setBigDecimal(columnIndex, (BigDecimal) data);
        break;
      case CLASS_TIMESTAMP:
        insertStatement.setTimestamp(columnIndex, (Timestamp) data);
        break;
      case CLASS_DATE:
        insertStatement.setDate(columnIndex, (Date) data);
        break;
      case CLASS_FLOAT:
        insertStatement.setFloat(columnIndex, (Float) data);
        break;
      case CLASS_SHORT:
        insertStatement.setShort(columnIndex, (Short) data);
        break;
      case CLASS_TIME:
        insertStatement.setTime(columnIndex, (Time) data);
        break;
      case CLASS_OBJECT:
        insertStatement.setObject(columnIndex, data);
        break;
      default:
        throw new UnhandledColumnTypeException("Unhandled column type (" + this + ")");
    }

    return result;
  }

  private boolean driverSupportsStream(final DatabaseType databaseType)
  {
    return !(DatabaseType.POSTGRESQL.equals(databaseType) || DatabaseType.DB2.equals(databaseType) || DatabaseType.MSSQL.equals(databaseType));
  }

  /**
   * @return classes handled by this type
   */
  public List<Class<?>> getColumnClasses()
  {
    return _columnClasses;
  }

  /**
   * Map class to {@link ColumnType}.
   */
  public static ColumnType valueOf(final Class<?> columnClass) throws SQLException
  {
    init();

    final ColumnType result = COLUMN_TYPES.get(columnClass);

    if (result == null)
    {
      throw new UnhandledColumnTypeException("Unhandled column class " + columnClass);
    }
    else
    {
      return result;
    }
  }

  public boolean isNumber()
  {
    return Number.class.isAssignableFrom(getColumnClasses().get(0));
  }

  /**
   * Check if class can be mapped to {@link ColumnType}.
   */
  public static boolean isSupportedClass(final Class<?> columnClass)
  {
    init();

    return COLUMN_TYPES.containsKey(columnClass);
  }

  /**
   * Check if class can be mapped to {@link ColumnType}.
   */
  public static boolean isSupportedClass(final String className) throws SQLException
  {
    final Class<?> clazz = forName(className);
    return isSupportedClass(clazz);
  }

  private static void init()
  {
    if (COLUMN_TYPES.isEmpty())
    {
      for (final ColumnType columnType : values())
      {
        for (final Class<?> columnClass : columnType.getColumnClasses())
        {
          COLUMN_TYPES.put(columnClass, columnType);
        }
      }
    }
  }

  public static ColumnType valueForClass(final String className) throws SQLException
  {
    final Class<?> clazz = forName(className);
    return valueOf(clazz);
  }

  private static Class<?> forName(final String className) throws SQLException
  {
    if ("byte[]".equals(className))
    { // Oracle-Bug
      return Util.ByteArrayClass;
    }
    else
    {
      try
      {
        return Class.forName(className);
      }
      catch (final ClassNotFoundException e)
      {
        throw new SQLException("Class not found: " + className, e);
      }
    }
  }

  private static class ClosableBlobWrapper implements Closeable
  {
    private final Blob _blob;

    public ClosableBlobWrapper(final Blob blob)
    {
      _blob = blob;
    }

    @Override
    public void close() throws IOException
    {
      try
      {
        _blob.free();
      }
      catch (SQLException e)
      {
        throw new IOException("close", e);
      }
    }
  }

  private static class ClosableClobWrapper implements Closeable
  {
    private final Clob _blob;

    public ClosableClobWrapper(final Clob blob)
    {
      _blob = blob;
    }

    @Override
    public void close() throws IOException
    {
      try
      {
        _blob.free();
      }
      catch (SQLException e)
      {
        throw new IOException("close", e);
      }
    }
  }

  private class ClosableSqlXmlWrapper implements Closeable
  {
    private final SQLXML _blob;

    public ClosableSqlXmlWrapper(final SQLXML blob)
    {
      _blob = blob;
    }

    @Override
    public void close() throws IOException
    {
      try
      {
        _blob.free();
      }
      catch (SQLException e)
      {
        throw new IOException("close", e);
      }
    }
  }
}
