package de.akquinet.jbosscc.guttenbase.statements;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Create INSERT statement with multiple VALUES tuples.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public abstract class AbstractInsertStatementCreator extends AbstractStatementCreator {
	public static final String INSERT_INTO = "INSERT INTO ";

	public AbstractInsertStatementCreator(final ConnectorRepository connectorRepository, final String connectorId) {
		super(connectorRepository, connectorId);
	}

	/**
	 * Create INSERT statement for the mapped target columns.
	 */
	public PreparedStatement createInsertStatement(final String sourceConnectorId, final TableMetaData sourceTableMetaData,
			final String targetTableName, final TableMetaData targetTableMetaData, final Connection destConnection,
			final int numberOfRowsPerBatch, final boolean useMultipleValuesClauses) throws SQLException {
		assert numberOfRowsPerBatch > 0 : "numberOfValueClauses > 0";

		final int numberOfValuesClauses = useMultipleValuesClauses ? numberOfRowsPerBatch : 1;
		final String sql = createSQL(sourceConnectorId, sourceTableMetaData, targetTableName, targetTableMetaData, numberOfValuesClauses);

		LOG.debug("Create INSERT statement " + sql);
		return destConnection.prepareStatement(sql);
	}

	private String createValueTuples(final int numberOfValuesClauses, final int columnCount) {
		final StringBuilder buf = new StringBuilder();

		for (int i = 0; i < numberOfValuesClauses; i++) {
			buf.append('(');

			for (int j = 0; j < columnCount; j++) {
				buf.append('?');

				if (j < columnCount - 1) {
					buf.append(',');
				}
			}

			buf.append(')');

			if (i < numberOfValuesClauses - 1) {
				buf.append(',');
			}
		}

		return buf.toString();
	}

	private String createSQL(final String sourceConnectorId, final TableMetaData sourceTableMetaData, final String targetTableName,
			final TableMetaData targetTableMetaData, final int numberOfValueClauses) throws SQLException {
		final List<ColumnMetaData> columns = getMappedTargetColumns(sourceTableMetaData, targetTableMetaData, sourceConnectorId);

		return INSERT_INTO + targetTableName + " (" + createColumnClause(columns) +
			") VALUES " +
			createValueTuples(numberOfValueClauses, columns.size()) +
			" " + createWhereClause(targetTableMetaData);
	}
}
