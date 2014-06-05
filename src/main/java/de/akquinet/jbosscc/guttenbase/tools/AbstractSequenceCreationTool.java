package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Create auto-increment sequences for table IDs.
 * <p/>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 * @Uses-Hint {@link de.akquinet.jbosscc.guttenbase.hints.TableNameMapperHint}
 * @Uses-Hint {@link de.akquinet.jbosscc.guttenbase.hints.EntityTableCheckerHint} to look for entity classes, i.e. classes that may use an ID sequence
 */
public abstract class AbstractSequenceCreationTool
{
  protected final ConnectorRepository _connectorRepository;
  protected final ScriptExecutorTool _scriptExecutor;

  public AbstractSequenceCreationTool(final ConnectorRepository connectorRepository)
  {
    assert connectorRepository != null : "connectorRepository != null";
    _connectorRepository = connectorRepository;
    _scriptExecutor = new ScriptExecutorTool(connectorRepository);
  }

  public void createSequences(final String connectorId, long start, long incrementBy) throws SQLException
  {
    final List<TableMetaData> tableMetaDatas = TableOrderHint.getSortedTables(_connectorRepository, connectorId);
    final EntityTableChecker entityTableChecker = _connectorRepository.getConnectorHint(connectorId, EntityTableChecker.class).getValue();
    final TableNameMapper tableNameMapper = _connectorRepository.getConnectorHint(connectorId, TableNameMapper.class).getValue();
    final List<String> updateClauses = new ArrayList<String>();

    for (final TableMetaData tableMetaData : tableMetaDatas)
    {
      if (entityTableChecker.isEntityTable(tableMetaData))
      {
        final String tableName = tableNameMapper.mapTableName(tableMetaData);
        final String sequenceName = getSequenceName(tableName);

        updateClauses.addAll(getCreateSequenceClauses(tableName, getIdColumn(tableMetaData), sequenceName, start, incrementBy));
      }
    }

    _scriptExecutor.executeScript(connectorId, false, false, updateClauses);
  }

  protected abstract String getIdColumn(final TableMetaData tableMetaData);

  protected abstract List<String> getCreateSequenceClauses(final String tableName, String idColumn, final String sequenceName, final long start, final long incrementBy);

  protected abstract String getSequenceName(final String tableName) throws SQLException;
}
