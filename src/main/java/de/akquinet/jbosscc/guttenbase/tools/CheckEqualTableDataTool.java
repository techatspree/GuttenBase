package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.SourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.connector.Connector;
import de.akquinet.jbosscc.guttenbase.connector.ConnectorInfo;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.exceptions.IncompatibleColumnsException;
import de.akquinet.jbosscc.guttenbase.exceptions.TableConfigurationException;
import de.akquinet.jbosscc.guttenbase.exceptions.UnequalDataException;
import de.akquinet.jbosscc.guttenbase.exceptions.UnequalNumberOfRowsException;
import de.akquinet.jbosscc.guttenbase.hints.ColumnOrderHint;
import de.akquinet.jbosscc.guttenbase.hints.NumberOfCheckedTableDataHint;
import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper.ColumnMapperResult;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapping;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.statements.SelectStatementCreator;
import org.apache.log4j.Logger;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Check two schemas for equal data where the tool takes a configurable number of sample data from each table.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 * @gb.UsesHint {@link TableNameMapperHint} to map table names
 * @gb.UsesHint {@link NumberOfCheckedTableDataHint} How many rows of tables shall be regarded when checking that data has been
 * transferred correctly.
 * @gb.UsesHint {@link ColumnNameMapperHint} to map column names
 * @gb.UsesHint {@link ColumnOrderHint} to determine column order
 * @gb.UsesHint {@link TableOrderHint} to determine order of tables
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
    final int numberOfCheckData = _connectorRepository.getConnectorHint(sourceConnectorId, NumberOfCheckedTableData.class)
      .getValue().getNumberOfCheckedTableData();

    final TableMapper tableMapper = _connectorRepository.getConnectorHint(targetConnectorId, TableMapper.class).getValue();
    final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(targetConnectorId);

    final SourceDatabaseConfiguration sourceDatabaseConfiguration1 = _connectorRepository
      .getSourceDatabaseConfiguration(sourceConnectorId);
    final SourceDatabaseConfiguration sourceDatabaseConfiguration2 = _connectorRepository
      .getSourceDatabaseConfiguration(targetConnectorId);
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

      checkTableData(sourceConnectorId, connection1, sourceDatabaseConfiguration1, tableSourceMetaData, targetConnectorId,
        connection2, sourceDatabaseConfiguration2, tableDestMetaData, numberOfCheckData);
    }

    sourceDatabaseConfiguration1.finalizeSourceConnection(connection1, sourceConnectorId);
    sourceDatabaseConfiguration2.finalizeSourceConnection(connection2, targetConnectorId);

    connector1.closeConnection();
    connector2.closeConnection();
  }

  private void checkTableData(final String sourceConnectorId, final Connection sourceConnection,
                              final SourceDatabaseConfiguration sourceConfiguration, final TableMetaData sourceTableMetaData,
                              final String targetConnectorId, final Connection targetConnection, final SourceDatabaseConfiguration targetConfiguration,
                              final TableMetaData targetTableMetaData, final int numberOfCheckData) throws SQLException {
    final String tableName1 = _connectorRepository.getConnectorHint(sourceConnectorId, TableMapper.class).getValue()
      .fullyQualifiedTableName(sourceTableMetaData, sourceTableMetaData.getDatabaseMetaData());
    final String tableName2 = _connectorRepository.getConnectorHint(targetConnectorId, TableMapper.class).getValue()
      .fullyQualifiedTableName(targetTableMetaData, targetTableMetaData.getDatabaseMetaData());
    final CommonColumnTypeResolverTool commonColumnTypeResolver = new CommonColumnTypeResolverTool(_connectorRepository);
    final ColumnMapper sourceColumnNameMapper = _connectorRepository.getConnectorHint(sourceConnectorId, ColumnMapper.class).getValue();
    final ColumnMapper targetColumnNameMapper = _connectorRepository.getConnectorHint(targetConnectorId, ColumnMapper.class).getValue();

    checkRowCount(sourceTableMetaData, targetTableMetaData, tableName1, tableName2);

    final PreparedStatement selectStatement1 = new SelectStatementCreator(_connectorRepository, sourceConnectorId)
      .createSelectStatement(sourceConnection, tableName1, sourceTableMetaData);
    selectStatement1.setFetchSize(numberOfCheckData);

    sourceConfiguration.beforeSelect(sourceConnection, sourceConnectorId, sourceTableMetaData);
    final ResultSet resultSet1 = selectStatement1.executeQuery();
    sourceConfiguration.afterSelect(sourceConnection, sourceConnectorId, sourceTableMetaData);

    final PreparedStatement selectStatement2 = new SelectStatementCreator(_connectorRepository, targetConnectorId)
      .createMappedSelectStatement(targetConnection, sourceTableMetaData, tableName2, targetTableMetaData, sourceConnectorId);
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
        int targetColumnIndex = 1;

        for (int sourceColumnIndex = 1; sourceColumnIndex <= orderedSourceColumns.size(); sourceColumnIndex++) {
          final ColumnMetaData sourceColumn = orderedSourceColumns.get(sourceColumnIndex - 1);
          final ColumnMapperResult mapping = columnMapper.map(sourceColumn, targetTableMetaData);

          for (final ColumnMetaData targetColumn : mapping.getColumns()) {
            final ColumnTypeMapping columnTypeMapping = commonColumnTypeResolver.getCommonColumnTypeMapping(
              sourceColumn, targetConnectorId, targetColumn);
            final String columnName1 = sourceColumnNameMapper.mapColumnName(sourceColumn, targetTableMetaData);
            final String columnName2 = targetColumnNameMapper.mapColumnName(targetColumn, targetTableMetaData);

            checkColumnTypeMapping(tableName1, sourceColumn, targetColumn, columnTypeMapping, columnName1, columnName2);

            final ColumnType sourceColumnType = columnTypeMapping.getSourceColumnType();

            checkData(sourceConnectorId, targetConnectorId, tableName1, resultSet1, resultSet2, rowIndex, targetColumnIndex, sourceColumnIndex, sourceColumn, targetColumn, columnTypeMapping, columnName1, sourceColumnType);
          }

          targetColumnIndex += mapping.getColumns().size();
        }

        rowIndex++;
      }
    } finally {
      closeEverything(selectStatement1, resultSet1, selectStatement2, resultSet2);
    }

    LOG.info("Checking data of " + tableName1 + " <--> " + tableName2 + " finished");
  }

  private void checkData(final String sourceConnectorId, final String targetConnectorId, final String tableName1,
                         final ResultSet resultSet1, final ResultSet resultSet2, final int rowIndex, final int targetColumnIndex,
                         final int sourceColumnIndex, final ColumnMetaData sourceColumn, final ColumnMetaData columnMetaData2, final ColumnTypeMapping columnTypeMapping, final String columnName1, final ColumnType sourceColumnType) throws SQLException {
    Object data1 = sourceColumnType.getValue(resultSet1, sourceColumnIndex);
    data1 = columnTypeMapping.getColumnDataMapper().map(sourceColumn, columnMetaData2, data1);
    Object data2 = columnTypeMapping.getTargetColumnType().getValue(resultSet2, targetColumnIndex);

    switch (sourceColumnType) {
      case CLASS_STRING:
        final ConnectorInfo connectionInfo1 = _connectorRepository.getConnectionInfo(sourceConnectorId);
        final ConnectorInfo connectionInfo2 = _connectorRepository.getConnectionInfo(targetConnectorId);

        // See http://www.postgresql.org/docs/8.3/static/datatype-character.html
        if (DatabaseType.POSTGRESQL.equals(connectionInfo1.getDatabaseType()) || DatabaseType.POSTGRESQL
          .equals(connectionInfo2.getDatabaseType())) {
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

      default:
        // No conversion needed
        break;
    }

    if ((data1 == null && data2 != null) || (data1 != null && data2 == null)) {
      throw createIncompatibleDataException(tableName1, rowIndex, sourceColumnType, columnName1, data1, data2);
    } else if (data1 != null && data2 != null && !data1.equals(data2)) {
      throw createIncompatibleDataException(tableName1, rowIndex, sourceColumnType, columnName1, data1, data2);
    }
  }

  private void checkRowCount(final TableMetaData sourceTableMetaData, final TableMetaData targetTableMetaData, final String tableName1, final String tableName2) throws UnequalNumberOfRowsException {
    if (sourceTableMetaData.getFilteredRowCount() != targetTableMetaData.getFilteredRowCount()) {
      throw new UnequalNumberOfRowsException("Number of rows is not equal: " + tableName1
        + "="
        + sourceTableMetaData.getFilteredRowCount()
        + " vs. "
        + tableName2
        + "="
        + targetTableMetaData.getFilteredRowCount());
    }

    LOG.info("Checking data of " + tableName1 + " <--> " + tableName2 + " started");
  }

  private void checkColumnTypeMapping(final String tableName1, final ColumnMetaData sourceColumn, final ColumnMetaData columnMetaData2, final ColumnTypeMapping columnTypeMapping, final String columnName1, final String columnName2) throws IncompatibleColumnsException {
    if (columnTypeMapping == null) {
      throw new IncompatibleColumnsException(tableName1 + ": Columns have incompatible types: "
        + columnName1
        + "/"
        + sourceColumn.getColumnTypeName()
        + " vs. "
        + columnName2
        + "/"
        + columnMetaData2.getColumnTypeName());
    }
  }

  private static void closeEverything(final PreparedStatement selectStatement1, final ResultSet resultSet1,
                                      final PreparedStatement selectStatement2, final ResultSet resultSet2) {
    try {
      resultSet1.close();
      selectStatement1.close();
      resultSet2.close();
      selectStatement2.close();
    } catch (final Exception e) {
      LOG.warn("Closing", e);
    }
  }

  private static String createStringFromBlob(final Blob blob) throws SQLException {
    return blob == null ? null : new String(blob.getBytes(1, (int) Math.min(blob.length(), 1000)));
  }

  private static String trim(final String data) {
    return data == null ? null : data.trim();
  }

  private static SQLException createIncompatibleDataException(final String tableName, final int index,
                                                              final ColumnType columnType, final String columnName, final Object data1, final Object data2) {
    return new UnequalDataException(tableName + ": Row "
      + index
      + ": Data not equal on column "
      + columnName
      + ": \n'"
      + data1
      + "'\n vs. \n'"
      + data2
      + "'\n, column class = "
      + columnType.getColumnClasses());
  }
}
