package de.akquinet.jbosscc.guttenbase.tools;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import de.akquinet.jbosscc.guttenbase.configuration.SourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.connector.Connector;
import de.akquinet.jbosscc.guttenbase.connector.ConnectorInfo;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.exceptions.IncompatibleColumnsException;
import de.akquinet.jbosscc.guttenbase.exceptions.TableConfigurationException;
import de.akquinet.jbosscc.guttenbase.exceptions.UnequalDataException;
import de.akquinet.jbosscc.guttenbase.exceptions.UnequalNumberOfRowsException;
import de.akquinet.jbosscc.guttenbase.hints.ColumnNameMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.ColumnOrderHint;
import de.akquinet.jbosscc.guttenbase.hints.NumberOfCheckedTableDataHint;
import de.akquinet.jbosscc.guttenbase.hints.TableNameMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapping;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.statements.SelectStatementCreator;

/**
 * Check two schemas for equal data where the tool takes a configurable number of sample data from each table.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @Uses-Hint {@link TableNameMapperHint} to map table names
 * @Uses-Hint {@link NumberOfCheckedTableDataHint} How many rows of tables shall be regarded when checking that data has been transferred
 *            correctly.
 * @Uses-Hint {@link ColumnNameMapperHint} to map column names
 * @Uses-Hint {@link ColumnOrderHint} to determine column order
 * @Uses-Hint {@link TableOrderHint} to determine order of tables
 * 
 * @author M. Dahm
 */
public class CheckEqualTableDataTool {
	private static final Logger LOG = Logger.getLogger(CheckEqualTableDataTool.class);

	private final ConnectorRepository _connectorRepository;

	public CheckEqualTableDataTool(final ConnectorRepository connectorRepository) {
		assert connectorRepository != null : "connectorRepository != null";
		_connectorRepository = connectorRepository;
	}

	public void checkTableData(final String sourceConnectorId, final String targetConnectorId) throws SQLException {
		final List<TableMetaData> tableSourceMetaDatas = TableOrderHint.getSortedTables(_connectorRepository, sourceConnectorId);
		final int numberOfCheckData = _connectorRepository.getConnectorHint(sourceConnectorId, NumberOfCheckedTableData.class).getValue()
				.getNumberOfCheckedTableData();

		final TableMapper tableMapper = _connectorRepository.getConnectorHint(targetConnectorId, TableMapper.class).getValue();
		final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(targetConnectorId);

		final SourceDatabaseConfiguration sourceDatabaseConfiguration1 = _connectorRepository.getSourceDatabaseConfiguration(sourceConnectorId);
		final SourceDatabaseConfiguration sourceDatabaseConfiguration2 = _connectorRepository.getSourceDatabaseConfiguration(targetConnectorId);
		final Connector connector1 = _connectorRepository.createConnector(sourceConnectorId);
		final Connector connector2 = _connectorRepository.createConnector(targetConnectorId);
		final Connection connection1 = connector1.openConnection();
		final Connection connection2 = connector2.openConnection();

		sourceDatabaseConfiguration1.initializeSourceConnection(connection1, sourceConnectorId);
		sourceDatabaseConfiguration2.initializeSourceConnection(connection2, targetConnectorId);

		for (final TableMetaData tableSourceMetaData : tableSourceMetaDatas) {
			final TableMetaData tableDestMetaData = tableMapper.map(tableSourceMetaData, targetDatabaseMetaData);

			if (tableDestMetaData == null) {
				throw new TableConfigurationException("No matching table for " + tableSourceMetaData + " in target data base!!!");
			}

			checkTableData(sourceConnectorId, connection1, sourceDatabaseConfiguration1, tableSourceMetaData, targetConnectorId, connection2,
					sourceDatabaseConfiguration2, tableDestMetaData, numberOfCheckData);
		}

		sourceDatabaseConfiguration1.finalizeSourceConnection(connection1, sourceConnectorId);
		sourceDatabaseConfiguration2.finalizeSourceConnection(connection2, targetConnectorId);

		connector1.closeConnection();
		connector2.closeConnection();
	}

	private void checkTableData(final String sourceConnectorId, final Connection sourceConnection,
			final SourceDatabaseConfiguration sourceConfiguration, final TableMetaData sourceTableMetaData, final String targetConnectorId,
			final Connection targetConnection, final SourceDatabaseConfiguration targetConfiguration, final TableMetaData targetTableMetaData,
			final int numberOfCheckData) throws SQLException {
		final String tableName1 = _connectorRepository.getConnectorHint(sourceConnectorId, TableNameMapper.class).getValue()
				.mapTableName(sourceTableMetaData);
		final String tableName2 = _connectorRepository.getConnectorHint(targetConnectorId, TableNameMapper.class).getValue()
				.mapTableName(targetTableMetaData);
		final CommonColumnTypeResolverTool commonColumnTypeResolver = new CommonColumnTypeResolverTool(_connectorRepository);
		final ColumnNameMapper sourceColumnNameMapper = _connectorRepository.getConnectorHint(sourceConnectorId, ColumnNameMapper.class)
				.getValue();
		final ColumnNameMapper targetColumnNameMapper = _connectorRepository.getConnectorHint(targetConnectorId, ColumnNameMapper.class)
				.getValue();

		if (sourceTableMetaData.getRowCount() != targetTableMetaData.getRowCount()) {
			throw new UnequalNumberOfRowsException("Number of rows is not equal: " + tableName1 + "=" + sourceTableMetaData.getRowCount()
					+ " vs. " + tableName2 + "=" + targetTableMetaData.getRowCount());
		}

		LOG.info("Checking data of " + tableName1 + " <--> " + tableName2 + " started");

		final PreparedStatement selectStatement1 = new SelectStatementCreator(_connectorRepository, sourceConnectorId).createSelectStatement(
				tableName1, sourceTableMetaData, sourceConnection);
		selectStatement1.setFetchSize(numberOfCheckData);

		sourceConfiguration.beforeSelect(sourceConnection, sourceConnectorId, sourceTableMetaData);
		final ResultSet resultSet1 = selectStatement1.executeQuery();
		sourceConfiguration.afterSelect(sourceConnection, sourceConnectorId, sourceTableMetaData);

		final PreparedStatement selectStatement2 = new SelectStatementCreator(_connectorRepository, targetConnectorId)
				.createMappedSelectStatement(sourceTableMetaData, tableName2, targetTableMetaData, targetConnection, sourceConnectorId);
		selectStatement2.setFetchSize(numberOfCheckData);

		targetConfiguration.beforeSelect(targetConnection, targetConnectorId, targetTableMetaData);
		final ResultSet resultSet2 = selectStatement2.executeQuery();
		targetConfiguration.afterSelect(targetConnection, targetConnectorId, targetTableMetaData);

		final List<ColumnMetaData> orderedSourceColumns = ColumnOrderHint.getSortedColumns(_connectorRepository, sourceConnectorId,
				sourceTableMetaData);
		final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(targetConnectorId, ColumnMapper.class).getValue();

		int rowIndex = 1;

		try {
			while (resultSet1.next() && resultSet2.next() && rowIndex <= numberOfCheckData) {
				for (int columnIndex = 1; columnIndex <= orderedSourceColumns.size(); columnIndex++) {
					final ColumnMetaData sourceColumn = orderedSourceColumns.get(columnIndex - 1);
					final List<ColumnMetaData> targetColumns = columnMapper.map(sourceColumn, targetTableMetaData);

					for (final ColumnMetaData columnMetaData2 : targetColumns) {
						final ColumnTypeMapping columnTypeMapping = commonColumnTypeResolver.getCommonColumnTypeMapping(sourceConnectorId,
								sourceColumn, targetConnectorId, columnMetaData2);
						final String columnName1 = sourceColumnNameMapper.mapColumnName(sourceColumn);
						final String columnName2 = targetColumnNameMapper.mapColumnName(columnMetaData2);

						if (columnTypeMapping == null) {
							throw new IncompatibleColumnsException(tableName1 + ": Columns have incompatible types: " + columnName1 + "/"
									+ sourceColumn.getColumnTypeName() + " vs. " + columnName2 + "/" + columnMetaData2.getColumnTypeName());
						}

						final ColumnType sourceColumnType = columnTypeMapping.getSourceColumnType();
						Object data1 = sourceColumnType.getValue(resultSet1, columnIndex);
						data1 = columnTypeMapping.getColumnDataMapper().map(sourceColumn, columnMetaData2, data1);
						Object data2 = columnTypeMapping.getTargetColumnType().getValue(resultSet2, columnIndex);

						switch (sourceColumnType) {
						case CLASS_STRING:
							final ConnectorInfo connectionInfo1 = _connectorRepository.getConnectionInfo(sourceConnectorId);
							final ConnectorInfo connectionInfo2 = _connectorRepository.getConnectionInfo(targetConnectorId);

							// See http://www.postgresql.org/docs/8.3/static/datatype-character.html
							if (DatabaseType.POSTGRESQL.equals(connectionInfo1.getDatabaseType())
									|| DatabaseType.POSTGRESQL.equals(connectionInfo2.getDatabaseType())) {
								data1 = trim((String) data1);
								data2 = trim((String) data2);
							}
							break;
						case CLASS_BLOB:
							final Blob blob1 = (Blob) data1;
							final Blob blob2 = (Blob) data2;
							data1 = createStringFromBlob(blob1);
							data2 = createStringFromBlob(blob2);
							break;
						}

						if ((data1 == null && data2 != null) || (data1 != null && data2 == null)) {
							throw createIncompatibleDataException(tableName1, rowIndex, sourceColumnType, columnName1, data1, data2);
						} else if (data1 != null && data2 != null && !data1.equals(data2)) {
							throw createIncompatibleDataException(tableName1, rowIndex, sourceColumnType, columnName1, data1, data2);
						}
					}
				}

				rowIndex++;
			}
		} finally {
			try {
				resultSet1.close();
				selectStatement1.close();
				resultSet2.close();
				selectStatement2.close();
			} catch (final Exception e) {
			}
		}

		LOG.info("Checking data of " + tableName1 + " <--> " + tableName2 + " finished");
	}

	private static String createStringFromBlob(final Blob blob) throws SQLException {
		return blob == null ? null : new String(blob.getBytes(1, (int) Math.min(blob.length(), 1000)));
	}

	private static String trim(final String data) {
		return data == null ? null : data.trim();
	}

	private static SQLException createIncompatibleDataException(final String tableName, final int index, final ColumnType columnType,
			final String columnName, final Object data1, final Object data2) {
		return new UnequalDataException(tableName + ": Row " + index + ": Data not equal on column " + columnName + ": '" + data1 + "' vs. '"
				+ data2 + "', column class = " + columnType.getColumnClasses());
	}
}
