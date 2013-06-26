package de.akquinet.jbosscc.guttenbase.tools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.akquinet.jbosscc.guttenbase.exceptions.IncompatibleColumnsException;
import de.akquinet.jbosscc.guttenbase.exceptions.IncompatibleTablesException;
import de.akquinet.jbosscc.guttenbase.hints.ColumnNameMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.ColumnOrderHint;
import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper.ColumnMapperResult;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapping;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Will check two schema's tables for compatibility.
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @Uses-Hint {@link ColumnNameMapperHint} to map column names
 * @Uses-Hint {@link TableOrderHint} to determine order of tables
 * @author M. Dahm
 */
public class CheckSchemaCompatibilityTool
{
  private static final Logger LOG = Logger.getLogger(CheckSchemaCompatibilityTool.class);

  private final ConnectorRepository _connectorRepository;

  public CheckSchemaCompatibilityTool(final ConnectorRepository connectorRepository)
  {
    assert connectorRepository != null : "connectorRepository != null";
    _connectorRepository = connectorRepository;
  }

  /**
   * Check compatibility of both connectors/schemata.
   * 
   * @throws SQLException
   */
  public void checkTableConfiguration(final String sourceConnectorId, final String targetConnectorId) throws SQLException
  {
    final List<TableMetaData> sourceTableMetaData = TableOrderHint.getSortedTables(_connectorRepository, sourceConnectorId);
    final TableMapper tableMapper = _connectorRepository.getConnectorHint(targetConnectorId, TableMapper.class).getValue();
    final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(targetConnectorId);

    checkEqualTables(sourceTableMetaData, targetDatabaseMetaData, tableMapper);

    for (final TableMetaData tableMetaData1 : sourceTableMetaData)
    {
      final TableMetaData tableMetaData2 = tableMapper.map(tableMetaData1, targetDatabaseMetaData);
      checkEqualColumns(sourceConnectorId, targetConnectorId, tableMetaData1, tableMetaData2);
    }
  }

  public void checkEqualColumns(final String sourceConnectorId, final String targetConnectorId,
      final TableMetaData tableMetaData1, final TableMetaData tableMetaData2) throws SQLException, IncompatibleColumnsException
  {
    final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(targetConnectorId, ColumnMapper.class).getValue();
    final CommonColumnTypeResolverTool commonColumnTypeResolver = new CommonColumnTypeResolverTool(_connectorRepository);
    final ColumnNameMapper sourceColumnNameMapper = _connectorRepository.getConnectorHint(sourceConnectorId,
        ColumnNameMapper.class).getValue();
    final ColumnNameMapper targetColumnNameMapper = _connectorRepository.getConnectorHint(targetConnectorId,
        ColumnNameMapper.class).getValue();

    final String tableName = tableMetaData1.getTableName();
    final List<ColumnMetaData> sourceColumns = ColumnOrderHint.getSortedColumns(_connectorRepository, sourceConnectorId,
        tableMetaData1);

    for (final ColumnMetaData columnMetaData1 : sourceColumns)
    {
      final ColumnMapperResult mapping = columnMapper.map(columnMetaData1, tableMetaData2);
      final List<ColumnMetaData> columnMetaDataTarget = mapping.getColumns();
      final String columnName1 = sourceColumnNameMapper.mapColumnName(columnMetaData1);

      if (columnMetaDataTarget.isEmpty())
      {
        if (mapping.isEmptyColumnListOk())
        {
          LOG.warn("No mapping column(s) found for: " + tableName + ":" + columnMetaData1 + " -> Will be dropped");
        }
        else
        {
          throw new IncompatibleColumnsException("No mapping column(s) found for: " + tableName + ":" + columnMetaData1);
        }
      }

      for (final ColumnMetaData columnMetaData2 : columnMetaDataTarget)
      {
        final String columnName2 = targetColumnNameMapper.mapColumnName(columnMetaData2);
        final ColumnTypeMapping columnTypeMapping = commonColumnTypeResolver.getCommonColumnTypeMapping(sourceConnectorId,
            columnMetaData1, targetConnectorId, columnMetaData2);

        if (columnTypeMapping == null)
        {
          throw new IncompatibleColumnsException(tableName + ":"
              + columnMetaData1
              + ": Columns have incompatible types: "
              + columnName1
              + "/"
              + columnMetaData1.getColumnTypeName()
              + "/"
              + columnMetaData1.getColumnClassName()
              + " vs. "
              + columnName2
              + "/"
              + columnMetaData2.getColumnTypeName()
              + "/"
              + columnMetaData2.getColumnClassName());
        }
      }
    }
  }

  private void checkEqualTables(final List<TableMetaData> sourceTableMetaData, final DatabaseMetaData targetDatabaseMetaData,
      final TableMapper tableMapper) throws SQLException
  {
    final List<TableMetaData> missingData = new ArrayList<TableMetaData>();

    for (final TableMetaData tableMetaData : sourceTableMetaData)
    {
      final TableMetaData targetTableMetaData = tableMapper.map(tableMetaData, targetDatabaseMetaData);

      if (targetTableMetaData == null)
      {
        missingData.add(tableMetaData);
      }
    }

    if (!missingData.isEmpty())
    {
      throw new IncompatibleTablesException("Tables mismatch: Unknown/Unmapped source tables \n" + missingData);
    }
  }
}
