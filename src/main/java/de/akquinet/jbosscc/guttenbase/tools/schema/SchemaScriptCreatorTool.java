package de.akquinet.jbosscc.guttenbase.tools.schema;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.exceptions.IncompatibleColumnsException;
import de.akquinet.jbosscc.guttenbase.exceptions.IncompatibleTablesException;
import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.TableOrderTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.DuplicateIndexIssue;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssueType;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssues;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Create Custom DDL script from given database meta data.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class SchemaScriptCreatorTool {
  private static final Random RANDOM = new Random();

  private final String _sourceConnectorId;
  private final String _targetConnectorId;
  private final ConnectorRepository _connectorRepository;

  public SchemaScriptCreatorTool(final ConnectorRepository connectorRepository,
                                 final String sourceConnectorId, final String targetConnectorId) {
    assert connectorRepository != null : "connectorRepository != null";
    assert sourceConnectorId != null : "sourceConnectorId != null";
    assert targetConnectorId != null : "targetConnectorId != null";

    _sourceConnectorId = sourceConnectorId;
    _connectorRepository = connectorRepository;
    _targetConnectorId = targetConnectorId;
  }

  public List<String> createTableStatements() throws SQLException {
    final List<TableMetaData> tables = new TableOrderTool().getOrderedTables(getDatabaseMetaData().getTableMetaData(), true);

    return createTableStatements(tables);
  }

  public List<String> createTableStatements(final List<TableMetaData> tables) throws SQLException {
    final List<String> result = new ArrayList<>();

    for (final TableMetaData tableMetaData : tables) {
      result.add(createTable(tableMetaData));
    }

    return result;
  }

  public List<String> createPrimaryKeyStatements() throws SQLException {
    final List<TableMetaData> tables = new TableOrderTool().getOrderedTables(getDatabaseMetaData().getTableMetaData(), true);

    return createPrimaryKeyStatements(tables);
  }

  private DatabaseMetaData getDatabaseMetaData() throws SQLException {
    return _connectorRepository.getDatabaseMetaData(getSourceConnectorId());
  }

  public List<String> createPrimaryKeyStatements(final List<TableMetaData> tables) throws SQLException {
    final List<String> result = new ArrayList<>();

    for (final TableMetaData tableMetaData : tables) {
      int counter = 1;
      final List<ColumnMetaData> primaryKeyColumns = tableMetaData.getPrimaryKeyColumns();

      if (!primaryKeyColumns.isEmpty()) {
        result.add(createPrimaryKeyStatement(tableMetaData, primaryKeyColumns, counter++));
      }
    }

    return result;
  }

  public List<String> createIndexStatements() throws SQLException {
    final List<TableMetaData> tables = new TableOrderTool().getOrderedTables(getDatabaseMetaData().getTableMetaData(), true);

    return createIndexStatements(tables);
  }

  public List<String> createIndexStatements(final List<TableMetaData> tables) throws SQLException {
    final List<String> result = new ArrayList<>();

    for (final TableMetaData tableMetaData : tables) {
      int counter = 1;

      final SchemaCompatibilityIssues issues = new SchemaComparatorTool(_connectorRepository).checkDuplicateIndexes(tableMetaData);
      final List<IndexMetaData> conflictedIndexes = issues.getCompatibilityIssues().stream()
        .filter(i -> i.getCompatibilityIssueType() == SchemaCompatibilityIssueType.DUPLICATE_INDEX)
        .map(i -> ((DuplicateIndexIssue) i).getIndexMetaData()).collect(Collectors.toList());

      for (final IndexMetaData indexMetaData : tableMetaData.getIndexes()) {
        final List<ColumnMetaData> columns = indexMetaData.getColumnMetaData();
        final boolean columnsFormPrimaryKey = columns.stream().map(column -> column.isPrimaryKey()).reduce((a, b) -> a && b).orElse(false);
        final boolean conflictedIndex = conflictedIndexes.contains(indexMetaData);

        if (!columnsFormPrimaryKey && !conflictedIndex) {
          result.add(createIndex(indexMetaData, counter++));
        }
      }
    }

    return result;
  }

  public List<String> createForeignKeyStatements() throws SQLException {
    final List<TableMetaData> tables = new TableOrderTool().getOrderedTables(getDatabaseMetaData().getTableMetaData(), true);

    return createForeignKeyStatements(tables);
  }

  public List<String> createForeignKeyStatements(final List<TableMetaData> tables) throws SQLException {
    final List<String> result = new ArrayList<>();

    for (final TableMetaData tableMetaData : tables) {
      int counter = 1;

      for (final ColumnMetaData columnMetaData : tableMetaData.getColumnMetaData()) {
        if (columnMetaData.getReferencedColumn() != null) {
          result.add(createForeignKey(columnMetaData, counter++));
        }
      }
    }

    return result;
  }

  public String createTable(final TableMetaData tableMetaData) throws SQLException {
    final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(getTargetConnectorId());
    final TableMapper tableMapper = _connectorRepository.getConnectorHint(getTargetConnectorId(), TableMapper.class).getValue();
    final String rawTableName = tableMapper.mapTableName(tableMetaData, targetDatabaseMetaData);
    final String tableName = tableMapper.fullyQualifiedTableName(tableMetaData, targetDatabaseMetaData);
    final StringBuilder builder = new StringBuilder("CREATE TABLE " + tableName + "\n(\n");
    final int maxNameLength = getTargetMaxNameLength();

    if (rawTableName.length() > maxNameLength) {
      throw new IncompatibleTablesException("Table name " + rawTableName + " is too long for the targeted data base (Max. "
        + maxNameLength + "). You will have to provide an appropriate " + TableMapper.class.getName() + " hint");
    }

    for (final Iterator<ColumnMetaData> iterator = tableMetaData.getColumnMetaData().iterator(); iterator.hasNext(); ) {
      final ColumnMetaData columnMetaData = iterator.next();

      builder.append("  ").append(createColumn(columnMetaData));

      if (iterator.hasNext()) {
        builder.append(", \n");
      }
    }

    return builder.append("\n);").toString();
  }


  private String createPrimaryKeyStatement(final TableMetaData tableMetaData, final List<ColumnMetaData> primaryKeyColumns,
                                           final int counter) throws SQLException {
    final TableMapper tableMapper = _connectorRepository.getConnectorHint(getTargetConnectorId(), TableMapper.class).getValue();
    final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(getTargetConnectorId(), ColumnMapper.class).getValue();
    final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(getTargetConnectorId());
    final String qualifiedTableName = tableMapper.fullyQualifiedTableName(tableMetaData, targetDatabaseMetaData);
    final String tableName = tableMapper.mapTableName(tableMetaData, targetDatabaseMetaData);
    final String pkName = createConstraintName("PK_", tableName + "_", counter);

    final StringBuilder builder = new StringBuilder("ALTER TABLE "
      + qualifiedTableName
      + " ADD CONSTRAINT " +
      pkName
      + " PRIMARY KEY (");

    for (final ColumnMetaData columnMetaData : primaryKeyColumns) {
      builder.append(columnMapper.mapColumnName(columnMetaData, tableMetaData)).append(", ");
    }

    builder.setLength(builder.length() - 2);
    builder.append(");");

    return builder.toString();
  }

  private String createIndex(final IndexMetaData indexMetaData, final int counter) throws SQLException {
    final TableMetaData tableMetaData = indexMetaData.getTableMetaData();
    final TableMapper tableMapper = _connectorRepository.getConnectorHint(getTargetConnectorId(), TableMapper.class).getValue();
    final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(getTargetConnectorId());
    final String tableName = tableMapper.mapTableName(tableMetaData, targetDatabaseMetaData);
    final String indexName = createConstraintName("IDX_", CaseConversionMode.UPPER.convert(indexMetaData.getIndexName())
      + "_" + tableName + "_", counter);

    return createIndex(indexMetaData, indexName);
  }

  public String createIndex(final IndexMetaData indexMetaData) throws SQLException {
    return createIndex(indexMetaData, indexMetaData.getIndexName());
  }

  private String createIndex(final IndexMetaData indexMetaData, final String indexName) throws SQLException {
    final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(getTargetConnectorId());
    final TableMapper tableMapper = _connectorRepository.getConnectorHint(getTargetConnectorId(), TableMapper.class).getValue();
    final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(getTargetConnectorId(), ColumnMapper.class).getValue();
    final TableMetaData tableMetaData = indexMetaData.getTableMetaData();
    final String unique = indexMetaData.isUnique() ? " UNIQUE " : " ";

    final StringBuilder builder = new StringBuilder("CREATE" + unique
      + "INDEX "
      + indexName
      + " ON "
      + tableMapper.fullyQualifiedTableName(tableMetaData, targetDatabaseMetaData)
      + "(");


    for (final Iterator<ColumnMetaData> iterator = indexMetaData.getColumnMetaData().iterator(); iterator.hasNext(); ) {
      final ColumnMetaData columnMetaData = iterator.next();

      builder.append(columnMapper.mapColumnName(columnMetaData, tableMetaData));

      if (iterator.hasNext()) {
        builder.append(", ");
      }
    }

    return builder.append(");").toString();
  }

  private String createForeignKey(final ColumnMetaData referencingColumn, final int counter) throws SQLException {
    final TableMapper tableMapper = _connectorRepository.getConnectorHint(getTargetConnectorId(), TableMapper.class).getValue();
    final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(getTargetConnectorId(), ColumnMapper.class).getValue();
    final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(getTargetConnectorId());
    final TableMetaData tableMetaData = referencingColumn.getTableMetaData();
    final ColumnMetaData referencedColumn = referencingColumn.getReferencedColumn();
    final String tablename = tableMapper.mapTableName(tableMetaData, targetDatabaseMetaData);
    final String fkName = createConstraintName("FK_", tablename + "_"
      + columnMapper.mapColumnName(referencingColumn, referencedColumn.getTableMetaData())
      + "_"
      + columnMapper.mapColumnName(referencedColumn, referencedColumn.getTableMetaData()) + "_", counter);

    return createForeignKey(referencingColumn, fkName);
  }

  public String createForeignKey(final ColumnMetaData referencingColumn, final String fkName) throws SQLException {
    final TableMapper tableMapper = _connectorRepository.getConnectorHint(getTargetConnectorId(), TableMapper.class).getValue();
    final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(getTargetConnectorId(), ColumnMapper.class).getValue();
    final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(getTargetConnectorId());
    final TableMetaData tableMetaData = referencingColumn.getTableMetaData();
    final ColumnMetaData referencedColumn = referencingColumn.getReferencedColumn();
    final String qualifiedTableName = tableMapper.fullyQualifiedTableName(tableMetaData, targetDatabaseMetaData);

    return "ALTER TABLE " + qualifiedTableName + " ADD CONSTRAINT " + fkName +
      " FOREIGN KEY (" + columnMapper.mapColumnName(referencingColumn, referencingColumn.getTableMetaData()) + ") REFERENCES " +
      tableMapper.fullyQualifiedTableName(referencedColumn.getTableMetaData(), targetDatabaseMetaData)
      + "(" + columnMapper.mapColumnName(referencedColumn, referencingColumn.getTableMetaData()) + ");";
  }

  public String createForeignKey(final ForeignKeyMetaData foreignKeyMetaData) throws SQLException {
    return createForeignKey(foreignKeyMetaData.getReferencingColumn(), foreignKeyMetaData.getForeignKeyName());
  }

  public String createColumn(final ColumnMetaData columnMetaData) throws SQLException {
    final TableMapper tableMapper = _connectorRepository.getConnectorHint(getTargetConnectorId(), TableMapper.class).getValue();
    final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(getTargetConnectorId());
    final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(getTargetConnectorId(), ColumnMapper.class).getValue();
    final ColumnTypeMapper columnTypeMapper = _connectorRepository.getConnectorHint(getTargetConnectorId(), ColumnTypeMapper.class).getValue();
    final StringBuilder builder = new StringBuilder();
    final DatabaseType sourceType = _connectorRepository.getDatabaseMetaData(getSourceConnectorId()).getDatabaseType();
    final DatabaseType targetType = _connectorRepository.getDatabaseMetaData(getTargetConnectorId()).getDatabaseType();
    final String columnName = columnMapper.mapColumnName(columnMetaData, columnMetaData.getTableMetaData());
    final String columnType = columnTypeMapper.mapColumnType(columnMetaData, sourceType, targetType);
    final int maxNameLength = getTargetMaxNameLength();
    final String rawTableName = tableMapper.mapTableName(columnMetaData.getTableMetaData(), targetDatabaseMetaData);

    if (columnName.length() > maxNameLength) {
      throw new IncompatibleColumnsException("Table " + rawTableName + ": Column name " + columnName + " is too long for " +
        "the targeted data base (Max. " + maxNameLength + ")." +
        " You will have to provide an appropriate " + ColumnMapper.class.getName() + " hint");
    }

    builder.append(columnName + " " + columnType);

    if (!columnMetaData.isNullable()) {
      builder.append(" NOT NULL");
    }

    return builder.toString();
  }

  public String createConstraintName(final String prefix, final String preferredName, final int uniqueId) throws SQLException {
    final StringBuilder name = new StringBuilder(preferredName);
    final int maxLength = getTargetMaxNameLength() - prefix.length() - String.valueOf(uniqueId).length();

    while (name.length() > maxLength) {
      final int index = Math.abs(RANDOM.nextInt() % name.length());
      name.deleteCharAt(index);
    }

    return prefix + name + uniqueId;
  }

  public int getTargetMaxNameLength() throws SQLException {
    final java.sql.DatabaseMetaData metaData = _connectorRepository.getDatabaseMetaData(getTargetConnectorId()).getDatabaseMetaData();

    // Since there is no getMaxConstraintNameLength() ...
    final int nameLength = metaData.getMaxColumnNameLength();

    // Return reasonable default if value is not known or unlimited
    return nameLength <= 0 ? 64 : nameLength;
  }

  public String getTargetConnectorId() {
    return _targetConnectorId;
  }

  public String getSourceConnectorId() {
    return _sourceConnectorId;
  }
}
