package de.akquinet.jbosscc.guttenbase.export;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.SQLException;

/**
 * Since CLOBs/BLOBs may be quite big. we do not load them into memory
 * completely, but read them in chunks and write the data to the output stream
 * in a loop.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public abstract class AbstractExportDumpObject implements Externalizable {
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_BUFFER_SIZE = 1024 * 1024 * 10;

	private transient File _tempFile;

	private transient final InputStream _inputStream;

	public AbstractExportDumpObject() {
		this(null);
	}

	public AbstractExportDumpObject(final InputStream inputStream) {
		_inputStream = inputStream;
	}

	/**
	 * Read data in chunks and write it to the outputstream to avoid out of memory
	 * errors.
	 */
	@Override
	public void writeExternal(final ObjectOutput output) throws IOException {
		final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

		for (int n = _inputStream.read(buffer); n > 0; n = _inputStream.read(buffer)) {
			byte[] buf = buffer;

			if (n < DEFAULT_BUFFER_SIZE) {
				buf = new byte[n];
				System.arraycopy(buffer, 0, buf, 0, n);
			}

			output.writeObject(buf);
			output.flush();
		}

		output.writeObject(null);
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

	public long length() throws SQLException {
		return _tempFile.length();
	}

	public byte[] getBytes(final long pos, final int length) throws SQLException {
		try {
			final InputStream inputStream = getBinaryStream(pos, length);
			final byte[] bytes = new byte[length];
			inputStream.read(bytes);
      inputStream.close();

			return bytes;
		} catch (final IOException e) {
			throw new SQLException("getBytes", e);
		}
	}

	public InputStream getBinaryStream() throws SQLException {
		return getBinaryStream(0, length());
	}

	public InputStream getBinaryStream(final long pos, final long length) throws SQLException {
		try {
			final FileInputStream inputStream = new FileInputStream(_tempFile);
			inputStream.skip(pos);
			return inputStream;
		} catch (final IOException e) {
			throw new SQLException("getBinaryStream", e);
		}
	}

	public void free() throws SQLException {
		_tempFile.delete();
		_tempFile = null;
	}
}
