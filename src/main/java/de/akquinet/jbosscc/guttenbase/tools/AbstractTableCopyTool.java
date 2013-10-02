package de.akquinet.jbosscc.guttenbase.tools;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

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
import de.akquinet.jbosscc.guttenbase.utils.Util;

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
  protected static final Logger LOG = Logger.getLogger(AbstractTableCopyTool.class);

  protected final ConnectorRepository _connectorRepository;

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
    final List<TableMetaData> tableSourceMetaDatas = TableOrderHint.getSortedTables(_connectorRepository, sourceConnectorId);
    final NumberOfRowsPerBatch numberOfRowsPerInsertionHint = _connectorRepository.getConnectorHint(targetConnectorId,
        NumberOfRowsPerBatch.class).getValue();
    final MaxNumberOfDataItems maxNumberOfDataItemsHint = _connectorRepository.getConnectorHint(targetConnectorId,
        MaxNumberOfDataItems.class).getValue();
    final long start = System.currentTimeMillis();

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

    int tableCounter = 1;
    for (final Iterator<TableMetaData> sourceTableIterator = tableSourceMetaDatas.iterator(); sourceTableIterator.hasNext(); tableCounter++)
    {
      final TableMetaData sourceTableMetaData = sourceTableIterator.next();
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
      final long startTimeCopyTable = System.currentTimeMillis();
      final int targetRowCount = targetTableMetaData.getRowCount();

      if (targetRowCount > 0)
      {
        LOG.warn("Target table " + targetTableMetaData.getTableName() + " is not empty!");
      }

      int numberOfRowsPerBatch = defaultNumberOfRowsPerBatch;
      final int columnCount = targetTableMetaData.getColumnCount();

      if (columnCount * numberOfRowsPerBatch > maxNumberOfDataItems)
      {
        numberOfRowsPerBatch = maxNumberOfDataItems / columnCount;
        LOG.debug("Max number of data items " + maxNumberOfDataItems
            + "exceeds numberOfValuesClauses * columns="
            + defaultNumberOfRowsPerBatch
            + " * "
            + columnCount
            + ". Cutting down number of VALUES clauses to "
            + numberOfRowsPerBatch);
      }

      sourceDatabaseConfiguration.beforeTableCopy(sourceConnection, sourceConnectorId, sourceTableMetaData);
      targetDatabaseConfiguration.beforeTableCopy(targetConnection, targetConnectorId, targetTableMetaData);

      LOG.info("Copying of " + sourceTableName
          + "-> "
          + targetTableName
          + "("
          + tableCounter
          + "/"
          + tableSourceMetaDatas.size()
          + ") started");
      copyTable(sourceConnectorId, sourceConnection, sourceDatabaseConfiguration, sourceTableMetaData, sourceTableName,
          targetConnectorId, targetConnection, targetDatabaseConfiguration, targetTableMetaData, targetTableName,
          numberOfRowsPerBatch, useMultipleValuesClauses);
      LOG.info("Copying of " + sourceTableName
          + "-> "
          + targetTableName
          + "("
          + tableCounter
          + "/"
          + tableSourceMetaDatas.size()
          + ") finished");

      sourceDatabaseConfiguration.afterTableCopy(sourceConnection, sourceConnectorId, sourceTableMetaData);
      targetDatabaseConfiguration.afterTableCopy(targetConnection, targetConnectorId, targetTableMetaData);

      final long endTimeCopyTable = System.currentTimeMillis();
      final long elapsedTime = (endTimeCopyTable - startTimeCopyTable);
      final long averagePerLine = elapsedTime / numberOfRowsPerBatch;

      LOG.info("Copying of " + sourceTableMetaData.getTableName() + " took " + Util.formatTime(elapsedTime));
      LOG.info(sourceTableMetaData.getRowCount() + " lines copied, average per batch ("
          + numberOfRowsPerBatch
          + ") = "
          + Util.formatTime(averagePerLine));
    }

    sourceDatabaseConfiguration.finalizeSourceConnection(sourceConnection, sourceConnectorId);
    targetDatabaseConfiguration.finalizeTargetConnection(targetConnection, targetConnectorId);

    sourceConnector.closeConnection();
    targetConnector.closeConnection();

    final long end = System.currentTimeMillis();

    LOG.info("Copying took " + Util.formatTime(end - start) + " total");

    _connectorRepository.refreshDatabaseMetaData(targetConnectorId);
  }

  protected abstract void copyTable(final String sourceConnectorId, final Connection sourceConnection,
      final SourceDatabaseConfiguration sourceDatabaseConfiguration, final TableMetaData sourceTableMetaData,
      final String sourceTableName, final String targetConnectorId, final Connection targetConnection,
      final TargetDatabaseConfiguration targetDatabaseConfiguration, final TableMetaData targetTableMetaData,
      final String targetTableName, final int numberOfRowsPerBatch, boolean useMultipleValuesClauses) throws SQLException;
}
