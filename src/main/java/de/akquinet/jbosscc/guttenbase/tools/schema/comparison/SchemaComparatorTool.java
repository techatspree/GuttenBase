package de.akquinet.jbosscc.guttenbase.tools.schema.comparison;

import de.akquinet.jbosscc.guttenbase.hints.ColumnOrderHint;
import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper.ColumnMapperResult;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapping;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.CommonColumnTypeResolverTool;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Will check two schemas for compatibility and report found issues.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 * Hint is used by {@link TableOrderHint} to determine order of tables
 */
@SuppressWarnings("RedundantThrows")
public class SchemaComparatorTool {
  private final ConnectorRepository _connectorRepository;
  private final SchemaCompatibilityIssues _schemaCompatibilityIssues = new SchemaCompatibilityIssues();

  public SchemaComparatorTool(final ConnectorRepository connectorRepository) {
    assert connectorRepository != null : "connectorRepository != null";
    _connectorRepository = connectorRepository;
  }

  /**
   * Check compatibility of both connectors/schemata.
   *
   * @param sourceConnectorId
   * @param targetConnectorId
   * @return List of found issues. If empty the schemas are completely compatible
   * @throws SQLException
   */

  @SuppressWarnings("JavaDoc")
  public SchemaCompatibilityIssues check(final String sourceConnectorId, final String targetConnectorId) throws SQLException {
    final List<TableMetaData> sourceTables = TableOrderHint.getSortedTables(_connectorRepository, sourceConnectorId);
    final TableMapper tableMapper = _connectorRepository.getConnectorHint(targetConnectorId, TableMapper.class).getValue();
    final DatabaseMetaData targetDatabase = _connectorRepository.getDatabaseMetaData(targetConnectorId);

    checkEqualTables(sourceTables, targetDatabase, tableMapper);

    for (final TableMetaData sourceTable : sourceTables) {
      final TableMetaData targetTable = tableMapper.map(sourceTable, targetDatabase);

      if (targetTable != null) {
        checkEqualColumns(sourceConnectorId, targetConnectorId, sourceTable, targetTable);
        checkEqualForeignKeys(sourceTable, targetTable);
        checkEqualIndexes(sourceTable, targetTable);
        checkDuplicateIndexes(sourceTable);
        checkForeignKeys(sourceTable);
        checkDuplicateIndexes(targetTable);
        checkForeignKeys(targetTable);
      }
    }

    return _schemaCompatibilityIssues;
  }


  public SchemaCompatibilityIssues checkEqualForeignKeys(final TableMetaData sourceTable, final TableMetaData targetTable) throws SQLException {
    for (final ForeignKeyMetaData sourceFK : sourceTable.getImportedForeignKeys()) {
      ForeignKeyMetaData matchingFK = null;

      for (final ForeignKeyMetaData targetFK : targetTable.getImportedForeignKeys()) {
        if (sourceFK.getReferencedColumn().equals(targetFK.getReferencedColumn()) &&
          sourceFK.getTableMetaData().equals(targetFK.getTableMetaData()) &&
          sourceFK.getReferencingColumn().equals(targetFK.getReferencingColumn())) {
          matchingFK = targetFK;
        }
      }

      if (matchingFK == null) {
        _schemaCompatibilityIssues.addIssue(new MissingForeignKeyIssue("Missing/incompatible foreign key " + sourceFK, sourceFK));
      }
    }


    return _schemaCompatibilityIssues;
  }

  public SchemaCompatibilityIssues checkEqualIndexes(final TableMetaData sourceTable, final TableMetaData targetTable) throws SQLException {
    for (final IndexMetaData sourceIndex : sourceTable.getIndexes()) {
      IndexMetaData matchingIndex = null;

      for (final IndexMetaData targetIndex : targetTable.getIndexes()) {
        if (sourceIndex.getColumnMetaData().equals(targetIndex.getColumnMetaData())) {
          matchingIndex = targetIndex;
        }
      }

      if (matchingIndex == null) {
        _schemaCompatibilityIssues.addIssue(new MissingIndexIssue("Missing index " + sourceIndex, sourceIndex));
      }

    }


    return _schemaCompatibilityIssues;
  }


  public SchemaCompatibilityIssues checkDuplicateIndexes(final TableMetaData table) {
    final Map<String, IndexMetaData> indexMetaDataMap = new LinkedHashMap<>();

    for (final IndexMetaData index : table.getIndexes()) {
      final String sortedColumnNames = index.getColumnMetaData().stream().map(ColumnMetaData::getColumnName)
        .sorted().collect(Collectors.toList()).toString();

      if (indexMetaDataMap.containsKey(sortedColumnNames)) {
        final IndexMetaData conflictingIndex = indexMetaDataMap.get(sortedColumnNames);

        _schemaCompatibilityIssues.addIssue(new DuplicateIndexIssue("Duplicate index " + conflictingIndex + "vs." + index,
          index));

      } else {
        indexMetaDataMap.put(sortedColumnNames, index);
      }
    }

    return _schemaCompatibilityIssues;
  }

  public SchemaCompatibilityIssues checkForeignKeys(final TableMetaData table) {
    final Map<String, ForeignKeyMetaData> fkMetaDataMap = new LinkedHashMap<>();

    for (final ForeignKeyMetaData foreignKey : table.getExportedForeignKeys()) {
      final String columNames = getFullyQualifiedColumnName(foreignKey.getReferencingColumn()) + ":" +
        getFullyQualifiedColumnName(foreignKey.getReferencingColumn());

      if (fkMetaDataMap.containsKey(columNames)) {
        final ForeignKeyMetaData conflictingKey = fkMetaDataMap.get(columNames);

        _schemaCompatibilityIssues.addIssue(new DuplicateForeignKeyIssue("Duplicate foreignKey " + conflictingKey
          + "vs." + foreignKey, foreignKey));
      } else {
        fkMetaDataMap.put(columNames, foreignKey);
      }
    }

    return _schemaCompatibilityIssues;
  }

  private static String getFullyQualifiedColumnName(final ColumnMetaData columnMetaData) {
    return columnMetaData.getTableMetaData().getTableName() + "." + columnMetaData.getColumnName();
  }

  public SchemaCompatibilityIssues checkEqualColumns(final String sourceConnectorId, final String targetConnectorId,
                                                     final TableMetaData tableMetaData1, final TableMetaData tableMetaData2) throws SQLException {
    final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(targetConnectorId, ColumnMapper.class).getValue();
    final CommonColumnTypeResolverTool commonColumnTypeResolver = new CommonColumnTypeResolverTool(_connectorRepository);
    final ColumnMapper sourceColumnNameMapper = _connectorRepository.getConnectorHint(sourceConnectorId, ColumnMapper.class).getValue();
    final ColumnMapper targetColumnNameMapper = _connectorRepository.getConnectorHint(targetConnectorId, ColumnMapper.class).getValue();

    final String tableName = tableMetaData1.getTableName();
    final List<ColumnMetaData> sourceColumns = ColumnOrderHint.getSortedColumns(_connectorRepository, sourceConnectorId,
      tableMetaData1);
    final Set<ColumnMetaData> mappedTargetColumns = new HashSet<>(tableMetaData2.getColumnMetaData());

    for (final ColumnMetaData sourceColumn : sourceColumns) {
      final ColumnMapperResult mapping = columnMapper.map(sourceColumn, tableMetaData2);
      final List<ColumnMetaData> targetColumns = mapping.getColumns();
      final String sourceColumnName = sourceColumnNameMapper.mapColumnName(sourceColumn, tableMetaData1);

      if (targetColumns.isEmpty()) {
        if (mapping.isEmptyColumnListOk()) {
          _schemaCompatibilityIssues.addIssue(new DroppedColumnIssue("No mapping column(s) found for: " + tableName + ":" + sourceColumn + " -> Will be dropped", sourceColumn));
        } else {
          _schemaCompatibilityIssues.addIssue(new MissingColumnIssue("No mapping column(s) found for: " + tableName + ":" + sourceColumn, sourceColumn));
        }
      }

      mappedTargetColumns.removeAll(targetColumns);

      for (final ColumnMetaData targetColumn : targetColumns) {
        final String targetColumnName = targetColumnNameMapper.mapColumnName(targetColumn, tableMetaData2);
        final ColumnTypeMapping columnTypeMapping = commonColumnTypeResolver.getCommonColumnTypeMapping(
          sourceColumn, targetConnectorId, targetColumn);

        if (columnTypeMapping == null) {
          _schemaCompatibilityIssues.addIssue(new IncompatibleColumnsIssue(
            tableName + ":"
              + sourceColumn
              + ": Columns have incompatible types: "
              + sourceColumnName
              + "/"
              + sourceColumn.getColumnTypeName()
              + "/"
              + sourceColumn.getColumnClassName()
              + " vs. "
              + targetColumnName
              + "/"
              + targetColumn.getColumnTypeName()
              + "/"
              + targetColumn.getColumnClassName(), sourceColumn, targetColumn));
        }
      }
    }

    for (final ColumnMetaData targetColumn : mappedTargetColumns) {
      if (targetColumn.isNullable()) {
        _schemaCompatibilityIssues.addIssue(new AdditionalColumnIssue("Unmapped target column (Will be null): " +
          tableName + ":" + targetColumn, targetColumn));
      } else {
        _schemaCompatibilityIssues.addIssue(new AdditionalNonNullColumnIssue("Unmapped target column with not-null " +
          "constraint will cause error" + " : " +
          tableName + ":" + targetColumn, targetColumn));
      }
    }

    return _schemaCompatibilityIssues;
  }

  private void checkEqualTables(final List<TableMetaData> sourceTableMetaData, final DatabaseMetaData targetDatabaseMetaData,
                                final TableMapper tableMapper) throws SQLException {
    for (final TableMetaData tableMetaData : sourceTableMetaData) {
      final TableMetaData targetTableMetaData = tableMapper.map(tableMetaData, targetDatabaseMetaData);

      if (targetTableMetaData == null) {
        _schemaCompatibilityIssues.addIssue(new MissingTableIssue("Table " + tableMetaData + " is unknown/unmapped in target schema", tableMetaData));
      }
    }
  }
}
