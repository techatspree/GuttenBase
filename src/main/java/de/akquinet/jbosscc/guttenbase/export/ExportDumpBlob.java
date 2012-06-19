package de.akquinet.jbosscc.guttenbase.export;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * Since BLOBs may be quite big. we do not load them into memory completely, but read them in chunks and write the data to the output stream
 * in a loop.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ExportDumpBlob implements Externalizable, Blob {
	public static final int DEFAULT_BUFFER_SIZE = 1024 * 1024 * 10;

	private transient final Blob _blob;
	private transient File _tempFile;

	public ExportDumpBlob() {
		this(null);
	}

	public ExportDumpBlob(final Blob blob) {
		_blob = blob;
	}

	/**
	 * Read data in chunks and write it to the outputstream to avoid out of memory errors.
	 */
	@Override
	public void writeExternal(final ObjectOutput output) throws IOException {
		try {
			final InputStream inputStream = _blob.getBinaryStream();
			final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

			for (int n = inputStream.read(buffer); n > 0; n = inputStream.read(buffer)) {
				byte[] buf = buffer;

				if (n < DEFAULT_BUFFER_SIZE) {
					buf = new byte[n];
					System.arraycopy(buffer, 0, buf, 0, n);
				}

				output.writeObject(buf);
				output.flush();
			}

			output.writeObject(null);
		} catch (final SQLException e) {
			throw new IOException("writeExternal", e);
		}
	}

	/**
	 * Store read data in temporary file to avoid out of memory errors.
	 */
	@Override
	public void readExternal(final ObjectInput input) throws IOException, ClassNotFoundException {
		_tempFile = File.createTempFile("GB-DUMP-", null);
		_tempFile.deleteOnExit();
		final FileOutputStream fileOutputStream = new FileOutputStream(_tempFile);

		for (byte[] buffer = (byte[]) input.readObject(); buffer != null; buffer = (byte[]) input.readObject()) {
			fileOutputStream.write(buffer, 0, buffer.length);
		}

		fileOutputStream.close();
	}

	@Override
	public long length() throws SQLException {
		return _tempFile.length();
	}

	@Override
	public byte[] getBytes(final long pos, final int length) throws SQLException {
		try {
			final InputStream inputStream = getBinaryStream(pos, length);
			final byte[] bytes = new byte[length];
			inputStream.read(bytes);

			return bytes;
		} catch (final IOException e) {
			throw new SQLException("getBytes", e);
		}
	}

	@Override
	public InputStream getBinaryStream() throws SQLException {
		return getBinaryStream(0, length());
	}

	@Override
	public InputStream getBinaryStream(final long pos, final long length) throws SQLException {
		try {
			final FileInputStream inputStream = new FileInputStream(_tempFile);
			inputStream.skip(pos);
			return inputStream;
		} catch (final IOException e) {
			throw new SQLException("getBinaryStream", e);
		}
	}

	@Override
	public long position(final byte[] pattern, final long start) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public long position(final Blob pattern, final long start) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int setBytes(final long pos, final byte[] bytes) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int setBytes(final long pos, final byte[] bytes, final int offset, final int len) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public OutputStream setBinaryStream(final long pos) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void truncate(final long len) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void free() throws SQLException {
		_tempFile.delete();
		_tempFile = null;
	}
}
