package de.akquinet.jbosscc.guttenbase.export;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * Since CLOBs may be quite big. we do not load them into memory completely, but read them in chunks and write the data to the output stream
 * in a loop.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ExportDumpClob extends AbstractExportDumpObject implements Clob {
  private static final long serialVersionUID = 1L;

  public ExportDumpClob() {
    this(null);
  }

  public ExportDumpClob(final InputStream inputStream) {
    super(inputStream);
  }

  @Override
  public InputStream getAsciiStream() throws SQLException {
    return getBinaryStream();
  }

  @Override
  public Reader getCharacterStream() throws SQLException {
    return new InputStreamReader(getBinaryStream());
  }

  @Override
  public Reader getCharacterStream(final long pos, final long length) throws SQLException {
    return new InputStreamReader(getBinaryStream(pos, length));
  }

  @Override
  public String getSubString(final long pos, final int length) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public long position(final String searchstr, final long start) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public long position(final Clob searchstr, final long start) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public OutputStream setAsciiStream(final long pos) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Writer setCharacterStream(final long pos) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int setString(final long pos, final String str) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int setString(final long pos, final String str, final int offset, final int len) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void truncate(final long len) throws SQLException {
    throw new UnsupportedOperationException();
  }
}
