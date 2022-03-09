package de.akquinet.jbosscc.guttenbase.export;

import java.io.*;
import java.sql.Clob;

/**
 * Since CLOBs may be quite big. we do not load them into memory completely, but read them in chunks and write the data to the output stream
 * in a loop.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
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
  public InputStream getAsciiStream() {
    return getBinaryStream();
  }

  @Override
  public Reader getCharacterStream() {
    return new InputStreamReader(getBinaryStream());
  }

  @Override
  public Reader getCharacterStream(final long pos, final long length) {
    return new InputStreamReader(getBinaryStream(pos, length));
  }

  @Override
  public String getSubString(final long pos, final int length) {
    throw new UnsupportedOperationException();
  }

  @Override
  public long position(final String searchstr, final long start) {
    throw new UnsupportedOperationException();
  }

  @Override
  public long position(final Clob searchstr, final long start) {
    throw new UnsupportedOperationException();
  }

  @Override
  public OutputStream setAsciiStream(final long pos) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Writer setCharacterStream(final long pos) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int setString(final long pos, final String str) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int setString(final long pos, final String str, final int offset, final int len) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void truncate(final long len) {
    throw new UnsupportedOperationException();
  }
}
