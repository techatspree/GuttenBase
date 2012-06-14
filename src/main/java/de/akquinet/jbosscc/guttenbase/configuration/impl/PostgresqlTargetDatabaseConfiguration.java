package de.akquinet.jbosscc.guttenbase.configuration.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.hints.TableNameMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.postgresql.PostgresqlVacuumTablesTool;

/**
 * Implementation for PostgreSQL data base.
 * 
 * Running ANALYZE after insertions is recommended: http://www.postgresql.org/docs/7.4/static/populate.html
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @Uses-Hint {@link TableNameMapperHint}
 * @author M. Dahm
 */
public class PostgresqlTargetDatabaseConfiguration extends DefaultTargetDatabaseConfiguration {
	private final boolean _vacuumAfterCopy;

	public PostgresqlTargetDatabaseConfiguration(final ConnectorRepository connectorRepository) {
		this(connectorRepository, true);
	}

	/**
	 * @param connectorRepository
	 * @param vacuumAfterCopy
	 *          "Defragment" and optimize target table after copying
	 */
	public PostgresqlTargetDatabaseConfiguration(final ConnectorRepository connectorRepository, final boolean vacuumAfterCopy) {
		super(connectorRepository);
		_vacuumAfterCopy = vacuumAfterCopy;
	}

	@Override
	public void afterTableCopy(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException {
		if (_vacuumAfterCopy) {
			new PostgresqlVacuumTablesTool(_connectorRepository).executeOnTable(connectorId, false, false, table);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initializeTargetConnection(final Connection connection, final String connectorId) throws SQLException {
		connection.setAutoCommit(false);
		setReferentialIntegrity(connection, connectorId, getTableMetaData(connectorId), false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finalizeTargetConnection(final Connection connection, final String connectorId) throws SQLException {
		setReferentialIntegrity(connection, connectorId, getTableMetaData(connectorId), true);
	}

	private List<TableMetaData> getTableMetaData(final String connectorId) throws SQLException {
		return TableOrderHint.getSortedTables(_connectorRepository, connectorId);
	}

	private void setReferentialIntegrity(final Connection connection, final String connectorId, final List<TableMetaData> tableMetaDatas,
			final boolean enable) throws SQLException {
		for (final TableMetaData tableMetaData : tableMetaDatas) {
			final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(connectorId, TableNameMapper.class).getValue();
			final String tableName = tableNameMapper.mapTableName(tableMetaData);
			executeSQL(connection, "ALTER TABLE " + tableName + (enable ? " ENABLE " : " DISABLE ") + "TRIGGER ALL;");
		}
	}
}
