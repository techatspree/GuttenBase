package de.akquinet.jbosscc.guttenbase.export;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * Custom implementation that just reads everything into a byte array.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ImportDumpBlob implements Blob {
	private byte[] _bytes;

	public ImportDumpBlob(final byte[] bytes) {
		_bytes = bytes;
	}

	@Override
	public long length() throws SQLException {
		return _bytes.length;
	}

	@Override
	public byte[] getBytes(final long pos, final int length) throws SQLException {
		return _bytes;
	}

	@Override
	public InputStream getBinaryStream() throws SQLException {
		return new ByteArrayInputStream(_bytes);
	}

	@Override
	public void free() throws SQLException {
		_bytes = new byte[0];
	}

	@Override
	public long position(final byte[] pattern, final long start) throws SQLException {
		return 0;
	}

	@Override
	public long position(final Blob pattern, final long start) throws SQLException {
		return 0;
	}

	@Override
	public int setBytes(final long pos, final byte[] bytes) throws SQLException {
		return 0;
	}

	@Override
	public int setBytes(final long pos, final byte[] bytes, final int offset, final int len) throws SQLException {
		return 0;
	}

	@Override
	public OutputStream setBinaryStream(final long pos) throws SQLException {
		return null;
	}

	@Override
	public void truncate(final long len) throws SQLException {
	}

	@Override
	public InputStream getBinaryStream(final long pos, final long length) throws SQLException {
		return null;
	}
}
