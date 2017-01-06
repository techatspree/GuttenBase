package de.akquinet.jbosscc.guttenbase.tools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.hints.EntityTableCheckerHint;
import de.akquinet.jbosscc.guttenbase.hints.TableNameMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Update auto-increment sequences for table IDs.
 *
 * By default the sequence is updated to SELECT(MAX(ID) + 1) FROM table
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @gb.UsesHint {@link TableNameMapperHint}
 * @gb.UsesHint {@link EntityTableCheckerHint} to look for entity classes, i.e. classes that may use an ID sequence
 * @author M. Dahm
 */
public abstract class AbstractSequenceUpdateTool {
	protected final ConnectorRepository _connectorRepository;
	protected final ScriptExecutorTool _scriptExecutor;
	protected final MinMaxIdSelectorTool _minMaxIdSelector;

	public AbstractSequenceUpdateTool(final ConnectorRepository connectorRepository) {
		assert connectorRepository != null : "connectorRepository != null";
		_connectorRepository = connectorRepository;
		_scriptExecutor = new ScriptExecutorTool(connectorRepository);
		_minMaxIdSelector = new MinMaxIdSelectorTool(connectorRepository);
	}

	public void updateSequences(final String connectorId) throws SQLException {
		final List<TableMetaData> tableMetaDatas = TableOrderHint.getSortedTables(_connectorRepository, connectorId);
		final EntityTableChecker entityTableChecker = _connectorRepository.getConnectorHint(connectorId, EntityTableChecker.class).getValue();
		final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(connectorId, TableNameMapper.class).getValue();
		final List<String> updateClauses = new ArrayList<String>();

		for (final TableMetaData tableMetaData : tableMetaDatas) {
			if (entityTableChecker.isEntityTable(tableMetaData)) {
				_minMaxIdSelector.computeMinMax(connectorId, tableMetaData);
				final long sequenceValue = _minMaxIdSelector.getMaxValue() + 1;
				final String tableName = tableNameMapper.mapTableName(tableMetaData);

				final String sequenceName = getSequenceName(tableName);
				updateClauses.add(getUpdateSequenceClause(sequenceName, sequenceValue));
			}
		}

		_scriptExecutor.executeScript(connectorId, false, false, updateClauses);
	}

	public abstract String getUpdateSequenceClause(String sequenceName, final long sequenceValue) throws SQLException;

	public abstract String getSequenceName(final String tableName) throws SQLException;
}
