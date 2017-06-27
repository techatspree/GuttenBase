package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.SourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.connector.Connector;
import de.akquinet.jbosscc.guttenbase.hints.ColumnOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapping;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.statements.SelectStatementCreator;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Read data from given table and put into a List of maps where each map contains the columns and values of a single line from
 * the table.
 * <p/>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ReadTableDataTool {
  private static final Logger LOG = Logger.getLogger(ReadTableDataTool.class);
  private final ConnectorRepository _connectorRepository;
  private final String _connectorId;
  private final TableMetaData _tableMetaData;
  private Connector _connector;
  private ResultSet _resultSet;

  public ReadTableDataTool(final ConnectorRepository connectorRepository, final String connectorId, final TableMetaData tableMetaData) {
    assert connectorRepository != null : "connectorRepository != null";
    assert connectorId != null : "connectorId != null";
    assert tableMetaData != null : "tableMetaData != null";

    _tableMetaData = tableMetaData;
    _connectorId = connectorId;
    _connectorRepository = connectorRepository;
  }

  public void start() throws SQLException {
    if (_connector == null) {
      final SourceDatabaseConfiguration sourceConfiguration = _connectorRepository.getSourceDatabaseConfiguration(_connectorId);
      _connector = _connectorRepository.createConnector(_connectorId);

      final Connection connection = _connector.openConnection();
      sourceConfiguration.initializeSourceConnection(connection, _connectorId);

      final TableMapper tableMapper = _connectorRepository.getConnectorHint(_connectorId, TableMapper.class).getValue();
      final DatabaseMetaData databaseMetaData = _connectorRepository.getDatabaseMetaData(_connectorId);
      final String tableName = tableMapper.fullyQualifiedTableName(_tableMetaData, databaseMetaData);
      final PreparedStatement selectStatement = new SelectStatementCreator(_connectorRepository, _connectorId)
        .createSelectStatement(connection, tableName, _tableMetaData);
      selectStatement.setFetchSize(512);

      sourceConfiguration.beforeSelect(connection, _connectorId, _tableMetaData);
      _resultSet = selectStatement.executeQuery();
      sourceConfiguration.afterSelect(connection, _connectorId, _tableMetaData);

    }
  }

  public void end() throws SQLException {
    if (_connector != null) {
      final SourceDatabaseConfiguration sourceConfiguration = _connectorRepository.getSourceDatabaseConfiguration(_connectorId);
      final Connection connection = _connector.openConnection();

      sourceConfiguration.finalizeSourceConnection(connection, _connectorId);

      _resultSet.close();
      _connector.closeConnection();
      _connector = null;
      _resultSet = null;
    }
  }


  /**
   * @param lines -1 means read all lines
   * @return list of maps containing table data or null of no more data is available
   */
  public List<Map<String, Object>> readTableData(int lines) throws SQLException {
    final List<Map<String, Object>> result = new ArrayList<>();
    final CommonColumnTypeResolverTool commonColumnTypeResolver = new CommonColumnTypeResolverTool(_connectorRepository);
    final ColumnMapper sourceColumnNameMapper = _connectorRepository.getConnectorHint(_connectorId, ColumnMapper.class).getValue();

    final List<ColumnMetaData> orderedSourceColumns = ColumnOrderHint.getSortedColumns(_connectorRepository, _connectorId,
      _tableMetaData);

    if (lines < 0) {
      lines = Integer.MAX_VALUE;
    }

    int rowIndex = 0;

    while (rowIndex < lines && _resultSet.next()) {
      final Map<String, Object> rowData = new HashMap<>();

      for (int columnIndex = 1; columnIndex <= orderedSourceColumns.size(); columnIndex++) {
        final ColumnMetaData sourceColumn = orderedSourceColumns.get(columnIndex - 1);
        final String columnName = sourceColumnNameMapper.mapColumnName(sourceColumn, _tableMetaData);
        final ColumnType sourceColumnType = commonColumnTypeResolver.getColumnType(_connectorId, sourceColumn);
        final ColumnTypeMapping columnTypeMapping = commonColumnTypeResolver.getCommonColumnTypeMapping(
          sourceColumn, _connectorId, sourceColumn);
        final Object data = sourceColumnType.getValue(_resultSet, columnIndex);
        final Object mappedData = columnTypeMapping.getColumnDataMapper().map(sourceColumn, sourceColumn, data);

        rowData.put(columnName, mappedData);
      }

      result.add(rowData);
      rowIndex++;
    }


    return rowIndex == 0 ? null : result;
  }
}
