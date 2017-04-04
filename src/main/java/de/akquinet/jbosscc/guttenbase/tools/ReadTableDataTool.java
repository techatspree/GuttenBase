package de.akquinet.jbosscc.guttenbase.tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.akquinet.jbosscc.guttenbase.configuration.SourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.connector.Connector;
import de.akquinet.jbosscc.guttenbase.hints.ColumnOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.statements.SelectStatementCreator;
import org.apache.log4j.Logger;

/**
 * Read data from given table and put into a map.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ReadTableDataTool {
	private static final Logger LOG = Logger.getLogger(ReadTableDataTool.class);
	private final ConnectorRepository _connectorRepository;

	public ReadTableDataTool(final ConnectorRepository connectorRepository) {
		assert connectorRepository != null : "connectorRepository != null";
		_connectorRepository = connectorRepository;
	}

	/**
	 * @param lines
	 *          -1 means read all lines
	 * @return map containing table data
	 */
	public List<Map<String, Object>> readTableData(final String connectorId, final TableMetaData tableMetaData, final int lines)
			throws SQLException {
		final SourceDatabaseConfiguration sourceDatabaseConfiguration = _connectorRepository.getSourceDatabaseConfiguration(connectorId);
		final Connector connector = _connectorRepository.createConnector(connectorId);
		final Connection connection = connector.openConnection();

		sourceDatabaseConfiguration.initializeSourceConnection(connection, connectorId);

		final List<Map<String, Object>> result = readTableData(connection, connectorId, sourceDatabaseConfiguration, tableMetaData,
				lines < 0 ? Integer.MAX_VALUE : lines);
		sourceDatabaseConfiguration.finalizeSourceConnection(connection, connectorId);
		connector.closeConnection();

		return result;
	}

	private List<Map<String, Object>> readTableData(final Connection connection, final String connectorId,
			final SourceDatabaseConfiguration sourceConfiguration, final TableMetaData tableMetaData, final int lines) throws SQLException {
		final List<Map<String, Object>> result = new ArrayList<>();
		final String tableName = _connectorRepository.getConnectorHint(connectorId, TableNameMapper.class).getValue()
				.mapTableName(tableMetaData);
		final CommonColumnTypeResolverTool commonColumnTypeResolver = new CommonColumnTypeResolverTool(_connectorRepository);
		final ColumnNameMapper sourceColumnNameMapper = _connectorRepository.getConnectorHint(connectorId, ColumnNameMapper.class).getValue();

		final PreparedStatement selectStatement = new SelectStatementCreator(_connectorRepository, connectorId).createSelectStatement(
						connection, tableName, tableMetaData);
		selectStatement.setFetchSize(lines);

		sourceConfiguration.beforeSelect(connection, connectorId, tableMetaData);
		final ResultSet resultSet = selectStatement.executeQuery();
		sourceConfiguration.afterSelect(connection, connectorId, tableMetaData);

		final List<ColumnMetaData> orderedSourceColumns = ColumnOrderHint.getSortedColumns(_connectorRepository, connectorId, tableMetaData);

		int rowIndex = 1;

		try {
			while (resultSet.next() && rowIndex <= lines) {
				final Map<String, Object> rowData = new HashMap<>();
				for (int columnIndex = 1; columnIndex <= orderedSourceColumns.size(); columnIndex++) {
					final ColumnMetaData sourceColumn = orderedSourceColumns.get(columnIndex - 1);
					final String columnName = sourceColumnNameMapper.mapColumnName(sourceColumn);
					final ColumnType sourceColumnType = commonColumnTypeResolver.getColumnType(connectorId, sourceColumn);
					final Object data = sourceColumnType.getValue(resultSet, columnIndex);

					rowData.put(columnName, data);
				}

				result.add(rowData);
				rowIndex++;
			}
		} finally {
			try {
				resultSet.close();
				selectStatement.close();
			} catch (final Exception e) {
				LOG.warn("Closing", e);
			}
		}

		return result;
	}
}
