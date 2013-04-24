package de.akquinet.jbosscc.guttenbase.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.akquinet.jbosscc.guttenbase.configuration.TargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.connector.Connector;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.sql.SQLLexer;
import de.akquinet.jbosscc.guttenbase.utils.Util;

/**
 * Execute given SQL script or single statements.
 * 
 * (C) 2012 by akquinet tech@spree
 * 
 * @author M. Dahm
 */
public class ScriptExecutorTool {
	private static final Logger LOG = Logger.getLogger(ScriptExecutorTool.class);
	private final ConnectorRepository _connectorRepository;

	public ScriptExecutorTool(final ConnectorRepository connectorRepository) {
		assert connectorRepository != null : "connectorRepository != null";
		_connectorRepository = connectorRepository;
	}

	/**
	 * Read SQL from file somewhere on class path.
	 */
	public void executeFileScript(final String connectorId, final String resourceName) throws SQLException {
		executeFileScript(connectorId, true, true, resourceName);
	}

	/**
	 * Read SQL from file somewhere on class path.
	 */
	public void executeFileScript(final String connectorId, final boolean updateSchema, final boolean prepareTargetConnection,
	    final String resourceName) throws SQLException {
		executeScript(connectorId, updateSchema, prepareTargetConnection, Util.readLinesFromFile(resourceName));
	}

	/**
	 * Execute given lines of SQL. Each statement (not line!) has end with a ';'
	 */
	public void executeScript(final String connectorId, final String... lines) throws SQLException {
		executeScript(connectorId, true, true, lines);
	}

	/**
	 * Execute given lines of SQL. Each statement (not line!) has end with a ';'
	 */
	public void executeScript(final String connectorId, final boolean updateSchema, final boolean prepareTargetConnection,
	    final String... lines) throws SQLException {
		executeScript(connectorId, updateSchema, prepareTargetConnection, Arrays.asList(lines));
	}

	/**
	 * Execute given lines of SQL. Each statement (not line!) end with a ';'
	 */
	public void executeScript(final String connectorId, final List<String> lines) throws SQLException {
		executeScript(connectorId, true, true, lines);
	}

	/**
	 * Execute given lines of SQL. Each statement (not line!) must end with a ';'
	 * 
	 * @param connectorId
	 * @param scriptUpdatesSchema
	 *          The script alters the schema, scheme information needs to be reloaded
	 * @param prepareTargetConnection
	 *          the target connection is initialized using the appropriate {@link TargetDatabaseConfiguration}
	 * @param lines
	 *          SQL statements ending with ';'
	 * @throws SQLException
	 */
	public void executeScript(final String connectorId, final boolean scriptUpdatesSchema, final boolean prepareTargetConnection,
	    final List<String> lines) throws SQLException {
		if (lines.isEmpty()) {
			throw new SQLException("DDL script not found or empty");
		}

		final List<String> sqlStatements = new SQLLexer(lines).parse();
		final Connector connector = _connectorRepository.createConnector(connectorId);
		final Connection connection = connector.openConnection();

		if (prepareTargetConnection) {
			final TargetDatabaseConfiguration targetDatabaseConfiguration = _connectorRepository
			    .getTargetDatabaseConfiguration(connectorId);
			targetDatabaseConfiguration.initializeTargetConnection(connection, connectorId);
		}

		final Statement statement = connection.createStatement();

		try {
			for (final String sql : sqlStatements) {
				executeSQL(statement, sql);
			}

			statement.close();

			if (prepareTargetConnection) {
				final TargetDatabaseConfiguration targetDatabaseConfiguration = _connectorRepository
				    .getTargetDatabaseConfiguration(connectorId);
				targetDatabaseConfiguration.finalizeTargetConnection(connection, connectorId);
			}
		} finally {
			connector.closeConnection();
		}

		if (scriptUpdatesSchema) {
			_connectorRepository.refreshDatabaseMetaData(connectorId);
		}
	}

	/**
	 * Execute query (i.e. SELECT...) and return the result set as a list of Maps where the key is the column name and the value the
	 * respective data.
	 * 
	 * @throws SQLException
	 */
	public List<Map<String, Object>> executeQuery(final String connectorId, final String sql) throws SQLException {
		final Connector connector = _connectorRepository.createConnector(connectorId);
		final List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		try {
			final Connection connection = connector.openConnection();
			final Statement statement = connection.createStatement();
			final ResultSet resultSet = statement.executeQuery(sql);
			readMapFromResultSet(result, resultSet);
			resultSet.close();
		} finally {
			connector.closeConnection();
		}

		return result;
	}

	private void readMapFromResultSet(final List<Map<String, Object>> result, final ResultSet resultSet) throws SQLException {
		final ResultSetMetaData metaData = resultSet.getMetaData();

		while (resultSet.next()) {
			final int columnCount = metaData.getColumnCount();
			final Map<String, Object> map = new HashMap<String, Object>();

			for (int i = 1; i <= columnCount; i++) {
				final String columnName = metaData.getColumnName(i);
				final Object value = resultSet.getObject(i);

				map.put(columnName, value);
			}

			result.add(map);
		}
	}

	private void executeSQL(final Statement statement, final String sql) throws SQLException {
		LOG.info("Executing: " + sql);

		final boolean result = statement.execute(sql);

		if (result) {
			final List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();

			final ResultSet resultSet = statement.getResultSet();
			readMapFromResultSet(resultMap, resultSet);
			resultSet.close();

			LOG.info("Query result: " + resultMap);
		} else {
			final int updateCount = statement.getUpdateCount();
			LOG.info("Update count: " + updateCount);
		}
	}
}
