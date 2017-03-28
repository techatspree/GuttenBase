package de.akquinet.jbosscc.guttenbase.statements;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.akquinet.jbosscc.guttenbase.hints.ColumnMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.ColumnNameMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.ColumnOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper.ColumnMapperResult;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Contains some helper methods for implementing classes.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @gb.UsesHint {@link ColumnNameMapperHint} to map column names
 * @gb.UsesHint {@link ColumnMapperHint} to map column names
 * @gb.UsesHint {@link ColumnOrderHint} to determine column order
 * @author M. Dahm
 */
public abstract class AbstractStatementCreator
{
  protected static final Logger LOG = Logger.getLogger(AbstractStatementCreator.class);

  protected final ConnectorRepository _connectorRepository;
  protected final String _connectorId;
  protected final ColumnNameMapper _columnNameMapper;

  public AbstractStatementCreator(final ConnectorRepository connectorRepository, final String connectorId)
  {
    assert connectorRepository != null : "connectorRepository != null";
    assert connectorId != null : "connectorId != null";
    _connectorId = connectorId;
    _connectorRepository = connectorRepository;
    _columnNameMapper = _connectorRepository.getConnectorHint(connectorId, ColumnNameMapper.class).getValue();
  }

  protected String createColumnClause(final List<ColumnMetaData> columns) throws SQLException
  {
    final StringBuilder columnBuf = new StringBuilder();

    for (final ColumnMetaData columnMetaData : columns)
    {
      columnBuf.append(_columnNameMapper.mapColumnName(columnMetaData) + ", ");
    }

    columnBuf.setLength(columnBuf.length() - 2);

    return columnBuf.toString();
  }

  protected String createWhereClause(final TableMetaData tableMetaData) throws SQLException
  {
    return "";
  }

  /**
   * Get the list of target columns with appropriate mappings as defined by {@link ColumnMapperHint}
   */
  public List<ColumnMetaData> getMappedTargetColumns(final TableMetaData sourceTableMetaData,
      final TableMetaData targetTableMetaData, final String sourceConnectorId) throws SQLException
  {
    // Use same order as in SELECT clause
    final List<ColumnMetaData> sourceColumns = ColumnOrderHint.getSortedColumns(_connectorRepository, sourceConnectorId,
        sourceTableMetaData);
    final List<ColumnMetaData> columns = new ArrayList<>();
    final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(_connectorId, ColumnMapper.class).getValue();

    for (final ColumnMetaData sourceColumnMetaData : sourceColumns)
    {
      final ColumnMapperResult mapping = columnMapper.map(sourceColumnMetaData, targetTableMetaData);
      columns.addAll(mapping.getColumns());
    }

    return columns;
  }
}
