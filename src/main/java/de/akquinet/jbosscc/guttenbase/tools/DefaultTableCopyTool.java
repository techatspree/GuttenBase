package de.akquinet.jbosscc.guttenbase.tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.configuration.SourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.TargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.statements.InsertStatementCreator;
import de.akquinet.jbosscc.guttenbase.statements.InsertStatementFiller;
import de.akquinet.jbosscc.guttenbase.statements.SelectStatementCreator;

/**
 * Copy all tables from one connection to the other with multiple VALUES-tuples per batch statement.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultTableCopyTool extends AbstractTableCopyTool
{
  public DefaultTableCopyTool(final ConnectorRepository connectorRepository)
  {
    super(connectorRepository);
  }

  /**
   * Copy data with multiple VALUES-tuples per batch statement.
   * 
   * @throws SQLException
   */
  @Override
  protected void copyTable(final String sourceConnectorId, final Connection sourceConnection,
      final SourceDatabaseConfiguration sourceDatabaseConfiguration, final TableMetaData sourceTableMetaData,
      final String sourceTableName, final String targetConnectorId, final Connection targetConnection,
      final TargetDatabaseConfiguration targetDatabaseConfiguration, final TableMetaData targetTableMetaData,
      final String targetTableName, final int numberOfRowsPerBatch, final boolean useMultipleValuesClauses) throws SQLException
  {
    final int sourceRowCount = sourceTableMetaData.getRowCount();
    final PreparedStatement selectStatement = new SelectStatementCreator(_connectorRepository, sourceConnectorId)
        .createSelectStatement(sourceTableName, sourceTableMetaData, sourceConnection);
    selectStatement.setFetchSize(Math.min(numberOfRowsPerBatch, selectStatement.getMaxRows()));

    sourceDatabaseConfiguration.beforeSelect(sourceConnection, sourceConnectorId, sourceTableMetaData);
    final ResultSet resultSet = selectStatement.executeQuery();
    sourceDatabaseConfiguration.afterSelect(sourceConnection, sourceConnectorId, sourceTableMetaData);

    final int numberOfBatches = sourceRowCount / numberOfRowsPerBatch;
    final int remainder = sourceRowCount - (numberOfBatches * numberOfRowsPerBatch);

    final InsertStatementCreator insertStatementCreator = new InsertStatementCreator(_connectorRepository, targetConnectorId);
    final InsertStatementFiller insertStatementFiller = new InsertStatementFiller(_connectorRepository);

    targetDatabaseConfiguration.beforeInsert(targetConnection, targetConnectorId, targetTableMetaData);
    final PreparedStatement batchInsertStatement = insertStatementCreator.createInsertStatement(sourceConnectorId,
        sourceTableMetaData, targetTableName, targetTableMetaData, targetConnection, numberOfRowsPerBatch,
        useMultipleValuesClauses);

    for (int i = 0; i < numberOfBatches; i++)
    {
      _progressIndicator.startBatch();

      insertStatementFiller.fillInsertStatementFromResultSet(sourceConnectorId, sourceTableMetaData, targetConnectorId,
          targetTableMetaData, targetDatabaseConfiguration, targetConnection, resultSet, batchInsertStatement,
          numberOfRowsPerBatch, useMultipleValuesClauses);

      batchInsertStatement.executeBatch();

      if (targetDatabaseConfiguration.isMayCommit())
      {
        targetConnection.commit();
      }

      _progressIndicator.endBatch((i + 1) * numberOfRowsPerBatch);
    }

    if (numberOfBatches > 0)
    {
      batchInsertStatement.close();
    }

    if (remainder > 0)
    {
      final PreparedStatement finalInsert = insertStatementCreator.createInsertStatement(sourceConnectorId, sourceTableMetaData,
          targetTableName, targetTableMetaData, targetConnection, remainder, useMultipleValuesClauses);
      insertStatementFiller.fillInsertStatementFromResultSet(sourceConnectorId, sourceTableMetaData, targetConnectorId,
          targetTableMetaData, targetDatabaseConfiguration, targetConnection, resultSet, finalInsert, remainder,
          useMultipleValuesClauses);
      finalInsert.executeBatch();

      if (targetDatabaseConfiguration.isMayCommit())
      {
        targetConnection.commit();
      }

      _progressIndicator.endBatch(sourceRowCount);

      finalInsert.close();
    }

    targetDatabaseConfiguration.afterInsert(targetConnection, targetConnectorId, targetTableMetaData);

    if (resultSet.next())
    {
      _progressIndicator.warn("Uncopied data!!!");
    }

    resultSet.close();
    selectStatement.close();
  }
}
