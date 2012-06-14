package de.akquinet.jbosscc.guttenbase.export;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import de.akquinet.jbosscc.guttenbase.exceptions.ExportException;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Special "{@link Connection}" that supports writing data to a different storage then a data base. I.e., a file dump.
 * 
 * Only few inherited methods have a meaningful implementation, most methods will throw a {@link UnsupportedOperationException}.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ExportDumpConnection implements Connection {
	private final Exporter _exporter;
	private boolean _closed;
	private final Set<TableMetaData> _exportedTables = new HashSet<TableMetaData>();
	private TableMetaData _currentTableMetaData;

	public ExportDumpConnection(final Exporter exporter) {
		assert exporter != null : "exporter != null";
		_exporter = exporter;
	}

	/**
	 * Returns PreparedStatement object for export.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public PreparedStatement prepareStatement(final String sql) throws SQLException {
		// Write header only once
		if (_exportedTables.add(_currentTableMetaData)) {
			writeTableHeader(_currentTableMetaData);
		}

		return new ExportDumpPreparedStatement(_exporter);
	}

	/**
	 * Simply forwards call to {@link Exporter}.
	 */
	public void initializeWriteTableData(final TableMetaData table) throws ExportException {
		assert table != null : "table != null";
		_currentTableMetaData = table;

		try {
			_exporter.initializeWriteTableData(table);
		} catch (final Exception e) {
			throw new ExportException("initializeWriteTableData", e);
		}
	}

	/**
	 * Simply forwards call to {@link Exporter}.
	 */
	public void finalizeWriteTableData(final TableMetaData table) throws ExportException {
		_currentTableMetaData = null;

		try {
			_exporter.finalizeWriteTableData(table);
		} catch (final Exception e) {
			throw new ExportException("finalizeWriteTableData", e);
		}
	}

	public void initializeWriteRowData(final TableMetaData table) throws ExportException {
		try {
			_exporter.initializeWriteRowData(table);
		} catch (final Exception e) {
			throw new ExportException("initializeWriteRowData", e);
		}
	}

	public void finalizeWriteRowData(final TableMetaData table) throws ExportException {
		try {
			_exporter.finalizeWriteRowData(table);
		} catch (final Exception e) {
			throw new ExportException("finalizeWriteRowData", e);
		}
	}

	/**
	 * Simply forwards call to {@link Exporter} which will close any open resources.
	 */
	@Override
	public void close() throws SQLException {
		try {
			_exporter.finishExport();
		} catch (final Exception e) {
			throw new ExportException("close", e);
		} finally {
			_closed = true;
			_exportedTables.clear();
		}
	}

	/**
	 * Simply forwards call to {@link Exporter} which may then flush its buffers.
	 */
	@Override
	public void commit() throws SQLException {
		try {
			_exporter.flush();
		} catch (final Exception e) {
			throw new ExportException("commit", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isClosed() {
		return _closed;
	}

	@Override
	public void setAutoCommit(final boolean autoCommit) throws SQLException {
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return false;
	}

	@Override
	public void setReadOnly(final boolean readOnly) throws SQLException {
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return false;
	}

	@Override
	public void rollback() throws SQLException {
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
		return prepareStatement(sql);
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
		return prepareStatement(sql);
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
		return prepareStatement(sql);
	}

	@Override
	public Statement createStatement() throws SQLException {
		return prepareStatement("");
	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public CallableStatement prepareCall(final String sql) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String nativeSQL(final String sql) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCatalog(final String catalog) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getCatalog() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTransactionIsolation(final int level) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearWarnings() throws SQLException {
	}

	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setHoldability(final int holdability) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getHoldability() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Savepoint setSavepoint(final String name) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void rollback(final Savepoint savepoint) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability)
			throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency,
			final int resultSetHoldability) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency,
			final int resultSetHoldability) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Clob createClob() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Blob createBlob() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public NClob createNClob() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isValid(final int timeout) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setClientInfo(final String name, final String value) throws SQLClientInfoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setClientInfo(final Properties properties) throws SQLClientInfoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getClientInfo(final String name) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Array createArrayOf(final String typeName, final Object[] elements) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
		throw new UnsupportedOperationException();
	}

	private void writeTableHeader(final TableMetaData tableMetaData) throws SQLException {
		try {
			_exporter.writeTableHeader(new ExportTableHeaderImpl(tableMetaData.getTableName()));
		} catch (final Exception e) {
			throw new ExportException("writeTableHeader", e);
		}
	}
}
