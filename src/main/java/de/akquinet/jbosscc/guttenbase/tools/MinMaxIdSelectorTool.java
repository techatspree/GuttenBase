package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.connector.Connector;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.statements.SplitByColumnSelectMinMaxStatementCreator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Compute MIN and MAX of given Id-Column
 * <p></p>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class MinMaxIdSelectorTool {
  private final ConnectorRepository _connectorRepository;
  private long _minValue;
  private long _maxValue;

  public MinMaxIdSelectorTool(final ConnectorRepository connectorRepository) {
    assert connectorRepository != null : "connectorRepository != null";
    _connectorRepository = connectorRepository;
  }

  /**
   * Compute MIN and MAX of given Id-Column
   */
  public void computeMinMax(final String connectorId, final TableMetaData tableMetaData) throws SQLException {
    final Connector connector = _connectorRepository.createConnector(connectorId);
    final Connection connection = connector.openConnection();

    computeMinMax(connectorId, tableMetaData, connection);

    connector.closeConnection();
  }

  /**
   * Compute MIN and MAX of given Id-Column using existing connection
   */
  public void computeMinMax(final String connectorId, final TableMetaData tableMetaData, final Connection connection) throws SQLException {
    final TableMapper tableMapper = _connectorRepository.getConnectorHint(connectorId, TableMapper.class).getValue();
    final String tableName = tableMapper.fullyQualifiedTableName(tableMetaData, tableMetaData.getDatabaseMetaData());
    final PreparedStatement minMaxStatement = new SplitByColumnSelectMinMaxStatementCreator(_connectorRepository, connectorId)
      .createSelectStatement(connection, tableName, tableMetaData);

    final ResultSet rangeResultSet = minMaxStatement.executeQuery();
    rangeResultSet.next();

    _minValue = rangeResultSet.getLong(1);
    _maxValue = rangeResultSet.getLong(2);
    minMaxStatement.close();
  }

  public long getMinValue() {
    return _minValue;
  }

  public long getMaxValue() {
    return _maxValue;
  }
}
