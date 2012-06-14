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
import de.akquinet.jbosscc.guttenbase.statements.SplitByColumnSelectCountStatementCreator;
import de.akquinet.jbosscc.guttenbase.statements.SplitByColumnSelectStatementCreator;

/**
 * Sometimes the amount of data exceeds any buffer. In these cases we need to split the data by some given range, usually the primary key.
 * I.e., the data is read in chunks where these chunks are split using the ID column range of values.
 * 
 * Copy all tables from one connection to the other splitting the input with the given column. If the number range is populated sparsely the
 * copying may take much longer than the {@link DefaultTableCopyTool}.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class SplitByRangeTableCopyTool extends AbstractTableCopyTool {
	public SplitByRangeTableCopyTool(final ConnectorRepository connectorRepository) {
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
		final InsertStatementCreator insertStatementCreator = new InsertStatementCreator(_connectorRepository, targetConnectorId);
		final InsertStatementFiller insertStatementFiller = new InsertStatementFiller(_connectorRepository);

		final MinMaxIdSelectorTool minMaxIdSelector = new MinMaxIdSelectorTool(_connectorRepository);
		minMaxIdSelector.computeMinMax(sourceConnectorId, sourceTableMetaData, sourceConnection);
		final long minValue = minMaxIdSelector.getMinValue();
		final long maxValue = minMaxIdSelector.getMaxValue();

		final PreparedStatement countStatement = new SplitByColumnSelectCountStatementCreator(_connectorRepository, sourceConnectorId)
				.createSelectStatement(sourceTableName, sourceTableMetaData, sourceConnection);

		final PreparedStatement selectStatement = new SplitByColumnSelectStatementCreator(_connectorRepository, sourceConnectorId)
				.createSelectStatement(sourceTableName, sourceTableMetaData, sourceConnection);
		selectStatement.setFetchSize(Math.min(numberOfValuesClauses, selectStatement.getMaxRows()));

		int totalWritten = 0;
		for (long splitColumnValue = minValue; splitColumnValue <= maxValue; splitColumnValue += numberOfValuesClauses + 1) {
			final long start = splitColumnValue;
			final long end = splitColumnValue + numberOfValuesClauses;

			sourceDatabaseConfiguration.beforeSelect(sourceConnection, sourceConnectorId, sourceTableMetaData);
			final long countData = getCurrentCount(countStatement, start, end);
			sourceDatabaseConfiguration.afterSelect(sourceConnection, sourceConnectorId, sourceTableMetaData);

			if (countData > 0) {
				LOG.debug(sourceTableName + ": " + " from " + start + " to " + end + " yields " + countData + " rows");
				selectStatement.setLong(1, start);
				selectStatement.setLong(2, end);

				sourceDatabaseConfiguration.beforeSelect(sourceConnection, sourceConnectorId, sourceTableMetaData);
				final ResultSet resultSet = selectStatement.executeQuery();
				sourceDatabaseConfiguration.afterSelect(sourceConnection, sourceConnectorId, sourceTableMetaData);

				targetDatabaseConfiguration.beforeInsert(targetConnection, targetConnectorId, targetTableMetaData);
				final PreparedStatement bulkInsert = insertStatementCreator.createInsertStatement(sourceConnectorId, sourceTableMetaData,
						targetTableName, targetTableMetaData, targetConnection, (int) countData);

				try {
					insertStatementFiller.fillInsertStatementFromResultSet(sourceConnectorId, sourceTableMetaData, targetConnectorId,
							targetTableMetaData, targetDatabaseConfiguration, targetConnection, resultSet, bulkInsert, (int) countData);
					bulkInsert.executeUpdate();
					targetConnection.commit();

					totalWritten += countData;
					LOG.debug(countData + " written / " + totalWritten + " total written / " + sourceRowCount + " rows ");

					if (resultSet.next()) {
						LOG.warn("Uncopied data!!!");
					}
				} catch (final SQLException e) {
					LOG.error("Error while copying from " + start + " TO " + end, e);
				} finally {
					resultSet.close();
					bulkInsert.close();
				}

				targetDatabaseConfiguration.afterInsert(targetConnection, targetConnectorId, targetTableMetaData);
			}
		}

		countStatement.close();
		selectStatement.close();
	}

	private long getCurrentCount(final PreparedStatement countStatement, final long start, final long end) throws SQLException {
		countStatement.setLong(1, start);
		countStatement.setLong(2, end);

		final ResultSet countQuery = countStatement.executeQuery();
		countQuery.next();
		return countQuery.getLong(1);
	}
}