package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.hints.EntityTableCheckerHint;
import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Update auto-increment sequences for table IDs.
 * <p></p>
 * By default the sequence is updated to SELECT(MAX(ID) + 1) FROM table
 * <p></p>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 * Uses {@link EntityTableCheckerHint} to look for entity classes, i.e. classes that may use an ID sequence
 */
@SuppressWarnings("RedundantThrows")
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
        final TableMapper tableMapper = _connectorRepository.getConnectorHint(connectorId, TableMapper.class).getValue();
        final List<String> updateClauses = new ArrayList<>();

        for (final TableMetaData tableMetaData : tableMetaDatas) {
            if (entityTableChecker.isEntityTable(tableMetaData)) {
                _minMaxIdSelector.computeMinMax(connectorId, tableMetaData);
                final long sequenceValue = _minMaxIdSelector.getMaxValue() + 1;
                final String tableName = tableMapper.mapTableName(tableMetaData, tableMetaData.getDatabaseMetaData());
                final String sequenceName = getSequenceName(tableName);

                updateClauses.add(getUpdateSequenceClause(sequenceName, sequenceValue));
            }
        }

        _scriptExecutor.executeScript(connectorId, false, false, updateClauses);
    }

    public abstract String getUpdateSequenceClause(String sequenceName, final long sequenceValue) throws SQLException;

    public abstract String getSequenceName(final String tableName) throws SQLException;
}
