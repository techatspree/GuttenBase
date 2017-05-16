package de.akquinet.jbosscc.guttenbase.tools.schema;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.hints.ConnectorHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.DefaultColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.TableOrderTool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
  public static final int MAX_ID_LENGTH = 64;

  private final String _targetConnectorId;
  private final int _maxIdLength;
  private final ConnectorRepository _connectorRepository;
  private final String _sourceConnectorId;

  public SchemaScriptCreatorTool(
          final ConnectorRepository connectorRepository,
          final String sourceConnectorId,
          final String targetConnectorId,
          final int maxIdLength) {
    assert connectorRepository != null : "connectorRepository != null";
    assert sourceConnectorId != null : "sourceConnectorId != null";
    assert targetConnectorId != null : "targetConnectorId != null";

    _sourceConnectorId = sourceConnectorId;
    _connectorRepository = connectorRepository;
    _targetConnectorId = targetConnectorId;
    _maxIdLength = maxIdLength;
  }

  public SchemaScriptCreatorTool(final ConnectorRepository connectorRepository, final String source, final String target) {
    this(connectorRepository, source, target, MAX_ID_LENGTH);
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
    final List<TableMetaData> tables = getDatabaseMetaData().getTableMetaData();
    return createPrimaryKeyStatements(tables);
  }

  private DatabaseMetaData getDatabaseMetaData() throws SQLException {
    return _connectorRepository.getDatabaseMetaData(_sourceConnectorId);
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
    final List<TableMetaData> tables = getDatabaseMetaData().getTableMetaData();
    return createIndexStatements(tables);
  }

  public List<String> createIndexStatements(final List<TableMetaData> tables) throws SQLException {
    final List<String> result = new ArrayList<>();

    for (final TableMetaData tableMetaData : tables) {
      int counter = 1;

      for (final IndexMetaData indexMetaData : tableMetaData.getIndexes()) {
        result.add(createIndex(indexMetaData, counter++));
      }
    }

    return result;
  }

  public List<String> createForeignKeyStatements() throws SQLException {
    final List<TableMetaData> tables = getDatabaseMetaData().getTableMetaData();
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
    final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(_targetConnectorId);
    final TableMapper tableMapper = _connectorRepository.getConnectorHint(_targetConnectorId, TableMapper.class).getValue();
    final StringBuilder builder = new StringBuilder("CREATE TABLE "
            + tableMapper.fullyQualifiedTableName(tableMetaData, targetDatabaseMetaData)
            + "\n(\n");

    for (final Iterator<ColumnMetaData> iterator = tableMetaData.getColumnMetaData().iterator(); iterator.hasNext(); ) {
      final ColumnMetaData columnMetaData = iterator.next();

      builder.append("  ").append(createColumn(columnMetaData));

      if (iterator.hasNext()) {
        builder.append(", \n");
      }
    }

    builder.append("\n);");
    return builder.toString();
  }


  private String createPrimaryKeyStatement(final TableMetaData tableMetaData, final List<ColumnMetaData> primaryKeyColumns,
                                           final int counter) throws SQLException {

    final TableMapper tableMapper = _connectorRepository.getConnectorHint(_targetConnectorId, TableMapper.class).getValue();
    final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(_targetConnectorId, ColumnMapper.class).getValue();
    final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(_targetConnectorId);
    final String qualifiedTableName = tableMapper.fullyQualifiedTableName(tableMetaData, targetDatabaseMetaData);
    final String pkName = "PK_" + tableMetaData.getTableName() + "_" + counter;
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
    final TableMapper tableMapper = _connectorRepository.getConnectorHint(_targetConnectorId, TableMapper.class).getValue();
    final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(_targetConnectorId);
    final String tableName = tableMapper.mapTableName(tableMetaData, targetDatabaseMetaData);
    final String indexName = createConstraintName("IDX_", CaseConversionMode.UPPER.convert(indexMetaData.getIndexName())
                    + "_"
                    + tableName
                    + "_",
            counter);
    return createIndex(indexMetaData, indexName);
  }

  public String createIndex(final IndexMetaData indexMetaData) throws SQLException {
    return createIndex(indexMetaData, indexMetaData.getIndexName());
  }

  private String createIndex(final IndexMetaData indexMetaData, final String indexName) throws SQLException {
    final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(_targetConnectorId);
    final TableMapper tableMapper = _connectorRepository.getConnectorHint(_targetConnectorId, TableMapper.class).getValue();
    final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(_targetConnectorId, ColumnMapper.class).getValue();
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

    builder.append(");");
    return builder.toString();
  }

  private String createForeignKey(final ColumnMetaData referencingColumn, final int counter) throws SQLException {
    final TableMapper tableMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, TableMapper.class).getValue();
    final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(_sourceConnectorId, ColumnMapper.class).getValue();
    final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(_targetConnectorId);
    final TableMetaData tableMetaData = referencingColumn.getTableMetaData();
    final ColumnMetaData referencedColumn = referencingColumn.getReferencedColumn();
    final String tablename = tableMapper.mapTableName(tableMetaData, targetDatabaseMetaData);

    String fkName = createConstraintName("FK_", tablename + "_"
            + columnMapper.mapColumnName(referencingColumn, referencedColumn.getTableMetaData())
            + "_"
            + columnMapper.mapColumnName(referencedColumn, referencedColumn.getTableMetaData()) + "_", counter);

    return createForeignKey(referencingColumn, fkName);
  }

  public String createForeignKey(final ColumnMetaData referencingColumn, final String fkName) throws SQLException {
    final TableMapper tableMapper = _connectorRepository.getConnectorHint(_targetConnectorId, TableMapper.class).getValue();
    final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(_targetConnectorId, ColumnMapper.class).getValue();
    final DatabaseMetaData targetDatabaseMetaData = _connectorRepository.getDatabaseMetaData(_targetConnectorId);
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

  private String createColumn(final ColumnMetaData columnMetaData) throws SQLException {
    final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(_targetConnectorId, ColumnMapper.class).getValue();
    final ColumnTypeMapper columnTypeMapper = getColumnTypeMapper();
    final StringBuilder builder = new StringBuilder();
    final DatabaseType sourceType = _connectorRepository.getDatabaseMetaData(_sourceConnectorId).getDatabaseType();
    final DatabaseType targetType = _connectorRepository.getDatabaseMetaData(_targetConnectorId).getDatabaseType();

    builder.append(columnMapper.mapColumnName(columnMetaData, columnMetaData.getTableMetaData())).append(" ")
            .append(columnTypeMapper.mapColumnType(columnMetaData, sourceType, targetType));

    if (!columnMetaData.isNullable()) {
      builder.append(" NOT NULL");
    }

    return builder.toString();
  }

  private ColumnTypeMapper getColumnTypeMapper() {
    final ConnectorHint<ColumnTypeMapper> hint = _connectorRepository.getConnectorHint(_sourceConnectorId, ColumnTypeMapper.class);

    if (hint == null) {
      return new DefaultColumnTypeMapper();
    }
    return hint.getValue();
  }

  public String createConstraintName(final String prefix, final String preferredName, final int uniqueId) {
    final StringBuilder name = new StringBuilder(preferredName);
    final int maxLength = _maxIdLength - prefix.length() - String.valueOf(uniqueId).length();

    while (name.length() > maxLength) {
      final int index = Math.abs(RANDOM.nextInt() % name.length());
      name.deleteCharAt(index);
    }

    return prefix + name + uniqueId;
  }
}
