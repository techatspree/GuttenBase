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
import de.akquinet.jbosscc.guttenbase.utils.Util;

/**
 * Copy all tables from one connection to the other with multiple VALUES-tuples per batch statement.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultTableCopyTool extends AbstractTableCopyTool {
	public DefaultTableCopyTool(final ConnectorRepository connectorRepository) {
		super(connectorRepository);
	}

	/**
	 * Copy data with multiple VALUES-tuples per batch statement.
	 * 
	 * @throws SQLException
	 */
	@Override
	protected void copyTable(final String sourceConnectorId, final Connection sourceConnection,
			final SourceDatabaseConfiguration sourceDatabaseConfiguration, final TableMetaData sourceTableMetaData, final String sourceTableName,
			final String targetConnectorId, final Connection targetConnection, final TargetDatabaseConfiguration targetDatabaseConfiguration,
			final TableMetaData targetTableMetaData, final String targetTableName, final int numberOfValuesClauses) throws SQLException {
		final int sourceRowCount = sourceTableMetaData.getRowCount();
		final PreparedStatement selectStatement = new SelectStatementCreator(_connectorRepository, sourceConnectorId).createSelectStatement(
				sourceTableName, sourceTableMetaData, sourceConnection);
		selectStatement.setFetchSize(Math.min(numberOfValuesClauses, selectStatement.getMaxRows()));

		sourceDatabaseConfiguration.beforeSelect(sourceConnection, sourceConnectorId, sourceTableMetaData);
		final ResultSet resultSet = selectStatement.executeQuery();
		sourceDatabaseConfiguration.afterSelect(sourceConnection, sourceConnectorId, sourceTableMetaData);

		final int bulkUpdateCount = sourceRowCount / numberOfValuesClauses;
		final int remainder = sourceRowCount - (bulkUpdateCount * numberOfValuesClauses);

		final InsertStatementCreator insertStatementCreator = new InsertStatementCreator(_connectorRepository, targetConnectorId);
		final InsertStatementFiller insertStatementFiller = new InsertStatementFiller(_connectorRepository);

		targetDatabaseConfiguration.beforeInsert(targetConnection, targetConnectorId, targetTableMetaData);
		final PreparedStatement bulkInsert = insertStatementCreator.createInsertStatement(sourceConnectorId, sourceTableMetaData,
				targetTableName, targetTableMetaData, targetConnection, numberOfValuesClauses);

		LOG.debug("Table row count = " + sourceRowCount + ", numberOfValuesClauses = " + numberOfValuesClauses + ", bulkUpdateCount = "
				+ bulkUpdateCount);

		for (int i = 0; i < bulkUpdateCount; i++) {
			final long startBatchTime = System.currentTimeMillis();

			insertStatementFiller.fillInsertStatementFromResultSet(sourceConnectorId, sourceTableMetaData, targetConnectorId,
					targetTableMetaData, targetDatabaseConfiguration, targetConnection, resultSet, bulkInsert, numberOfValuesClauses);

			bulkInsert.executeUpdate();
			targetConnection.commit();

			final long endBatchTime = System.currentTimeMillis();
			LOG.info(sourceTableName + ": " + ((i + 1) * numberOfValuesClauses) + "/" + sourceRowCount + " rows copied: last batch update took "
					+ Util.formatTime(endBatchTime - startBatchTime));
		}

		if (bulkUpdateCount > 0) {
			bulkInsert.close();
		}

		if (remainder > 0) {
			final PreparedStatement finalInsert = insertStatementCreator.createInsertStatement(sourceConnectorId, sourceTableMetaData,
					targetTableName, targetTableMetaData, targetConnection, remainder);
			insertStatementFiller.fillInsertStatementFromResultSet(sourceConnectorId, sourceTableMetaData, targetConnectorId,
					targetTableMetaData, targetDatabaseConfiguration, targetConnection, resultSet, finalInsert, remainder);
			finalInsert.executeUpdate();
			targetConnection.commit();
			finalInsert.close();
		}

		targetDatabaseConfiguration.afterInsert(targetConnection, targetConnectorId, targetTableMetaData);

		if (resultSet.next()) {
			LOG.warn("Uncopied data!!!");
		}

		resultSet.close();
		selectStatement.close();
	}
}
