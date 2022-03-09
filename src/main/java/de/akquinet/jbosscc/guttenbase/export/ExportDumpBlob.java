package de.akquinet.jbosscc.guttenbase.export;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;

/**
 * Since BLOBs may be quite big. we do not load them into memory completely, but
 * read them in chunks and write the data to the output stream in a loop.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ExportDumpBlob extends AbstractExportDumpObject implements Blob {
  private static final long serialVersionUID = 1L;

  public ExportDumpBlob() {
    this(null);
  }

  public ExportDumpBlob(final InputStream inputStream) {
    super(inputStream);
  }

  @Override
  public long position(final byte[] pattern, final long start) {
    throw new UnsupportedOperationException();
  }

  @Override
  public long position(final Blob pattern, final long start) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int setBytes(final long pos, final byte[] bytes) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int setBytes(final long pos, final byte[] bytes, final int offset, final int len) {
    throw new UnsupportedOperationException();
  }

  @Override
  public OutputStream setBinaryStream(final long pos) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void truncate(final long len) {
    throw new UnsupportedOperationException();
  }
}
