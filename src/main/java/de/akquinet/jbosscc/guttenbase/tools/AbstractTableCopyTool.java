package de.akquinet.jbosscc.guttenbase.tools;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.configuration.SourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.TargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.connector.Connector;
import de.akquinet.jbosscc.guttenbase.exceptions.TableConfigurationException;
import de.akquinet.jbosscc.guttenbase.hints.MaxNumberOfDataItemsHint;
import de.akquinet.jbosscc.guttenbase.hints.NumberOfRowsPerBatchHint;
import de.akquinet.jbosscc.guttenbase.hints.TableNameMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.utils.TableCopyProgressIndicator;

/**
 * Copy all tables from one connection to the other.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @Uses-Hint {@link TableNameMapperHint} to filter tables not to be regarded
 * @Uses-Hint {@link NumberOfRowsPerBatchHint} to determine number of VALUES clauses in INSERT statement
 * @Uses-Hint {@link MaxNumberOfDataItemsHint} to determine maximum number of data items in INSERT statement
 * @Uses-Hint {@link TableOrderHint} to determine order of tables
 * @author M. Dahm
 */
public abstract class AbstractTableCopyTool
{
  protected final ConnectorRepository _connectorRepository;
  protected TableCopyProgressIndicator _progressIndicator;

  public AbstractTableCopyTool(final ConnectorRepository connectorRepository)
  {
    assert connectorRepository != null : "connectorRepository != null";
    _connectorRepository = connectorRepository;
  }

  /**
   * Copy tables from source to target.
   */
  public final void copyTables(final String sourceConnectorId, final String targetConnectorId) throws SQLException
  {
    _progressIndicator = _connectorRepository.getConnectorHint(targetConnectorId, TableCopyProgressIndicator.class).getValue();
    _progressIndicator.initializeIndicator();

    final List<TableMetaData> tableSourceMetaDatas = TableOrderHint.getSortedTables(_connectorRepository, sourceConnectorId);
    final NumberOfRowsPerBatch numberOfRowsPerInsertionHint = _connectorRepository.getConnectorHint(targetConnectorId,
        NumberOfRowsPerBatch.class).getValue();
    final MaxNumberOfDataItems maxNumberOfDataItemsHint = _connectorRepository.getConnectorHint(targetConnectorId,
        MaxNumberOfDataItems.class).getValue();

    final SourceDatabaseConfiguration sourceDatabaseConfiguration = _connectorRepository
        .getSourceDatabaseConfiguration(sourceConnectorId);
    final TargetDatabaseConfiguration targetDatabaseConfiguration = _connectorRepository
        .getTargetDatabaseConfiguration(targetConnectorId);
    final Connector sourceConnector = _connectorRepository.createConnector(sourceConnectorId);
    final Connector targetConnector = _connectorRepository.createConnector(targetConnectorId);
    final Connection sourceConnection = sourceConnector.openConnection();
    final Connection targetConnection = targetConnector.openConnection();
    final TableNameMapper sourceTableNameMapper = _connectorRepository.getConnectorHint(sourceConnectorId, TableNameMapper.class)
        .getValue();
    final TableNameMapper targetTableNameMapper = _connectorRepository.getConnectorHint(targetConnectorId, TableNameMapper.class)
        .getValue();
    final TableMapper tableMapper = _connectorRepository.getConnectorHint(targetConnectorId, TableMapper.class).getValue();
    final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(targetConnectorId);

    sourceDatabaseConfiguration.initializeSourceConnection(sourceConnection, sourceConnectorId);
    targetDatabaseConfiguration.initializeTargetConnection(targetConnection, targetConnectorId);

    _progressIndicator.startProcess(tableSourceMetaDatas.size());

    for (final TableMetaData sourceTableMetaData : tableSourceMetaDatas)
    {
      final TableMetaData targetTableMetaData = tableMapper.map(sourceTableMetaData, targetDatabaseMetaData);
      final int defaultNumberOfRowsPerBatch = numberOfRowsPerInsertionHint.getNumberOfRowsPerBatch(targetTableMetaData);
      final boolean useMultipleValuesClauses = numberOfRowsPerInsertionHint.useMultipleValuesClauses(targetTableMetaData);
      final int maxNumberOfDataItems = maxNumberOfDataItemsHint.getMaxNumberOfDataItems(targetTableMetaData);

      if (targetTableMetaData == null)
      {
        throw new TableConfigurationException("No matching table for " + sourceTableMetaData + " in target data base!!!");
      }

      final String sourceTableName = sourceTableNameMapper.mapTableName(sourceTableMetaData);
      final String targetTableName = targetTableNameMapper.mapTableName(targetTableMetaData);
      final int targetRowCount = targetTableMetaData.getRowCount();

      if (targetRowCount > 0)
      {
        _progressIndicator.warn("Target table " + targetTableMetaData.getTableName() + " is not empty!");
      }

      int numberOfRowsPerBatch = defaultNumberOfRowsPerBatch;
      final int columnCount = targetTableMetaData.getColumnCount();

      if (columnCount * numberOfRowsPerBatch > maxNumberOfDataItems)
      {
        numberOfRowsPerBatch = maxNumberOfDataItems / columnCount;
        _progressIndicator.debug("Max number of data items " + maxNumberOfDataItems
            + " exceeds numberOfValuesClauses * columns="
            + defaultNumberOfRowsPerBatch
            + " * "
            + columnCount
            + ". Cutting down number of VALUES clauses to "
            + numberOfRowsPerBatch);
      }

      sourceDatabaseConfiguration.beforeTableCopy(sourceConnection, sourceConnectorId, sourceTableMetaData);
      targetDatabaseConfiguration.beforeTableCopy(targetConnection, targetConnectorId, targetTableMetaData);

      _progressIndicator
          .startCopyTable(sourceTableName, sourceTableMetaData.getRowCount(), targetTableName);

      copyTable(sourceConnectorId, sourceConnection, sourceDatabaseConfiguration, sourceTableMetaData, sourceTableName,
          targetConnectorId, targetConnection, targetDatabaseConfiguration, targetTableMetaData, targetTableName,
          numberOfRowsPerBatch, useMultipleValuesClauses);

      sourceDatabaseConfiguration.afterTableCopy(sourceConnection, sourceConnectorId, sourceTableMetaData);
      targetDatabaseConfiguration.afterTableCopy(targetConnection, targetConnectorId, targetTableMetaData);

      _progressIndicator.endProcess();
    }

    sourceDatabaseConfiguration.finalizeSourceConnection(sourceConnection, sourceConnectorId);
    targetDatabaseConfiguration.finalizeTargetConnection(targetConnection, targetConnectorId);

    sourceConnector.closeConnection();
    targetConnector.closeConnection();

    _progressIndicator.finalizeIndicator();
    _connectorRepository.refreshDatabaseMetaData(targetConnectorId);
  }

  protected abstract void copyTable(final String sourceConnectorId, final Connection sourceConnection,
      final SourceDatabaseConfiguration sourceDatabaseConfiguration, final TableMetaData sourceTableMetaData,
      final String sourceTableName, final String targetConnectorId, final Connection targetConnection,
      final TargetDatabaseConfiguration targetDatabaseConfiguration, final TableMetaData targetTableMetaData,
      final String targetTableName, final int numberOfRowsPerBatch, boolean useMultipleValuesClauses) throws SQLException;
}
