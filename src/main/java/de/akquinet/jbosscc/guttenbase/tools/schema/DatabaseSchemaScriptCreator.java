package de.akquinet.jbosscc.guttenbase.tools.schema;

import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultColumnMapper;
import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultTableMapper;
import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.tools.TableOrderTool;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Create DDL script from given database meta data.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
@SuppressWarnings("UnusedAssignment")
public class DatabaseSchemaScriptCreator {
  private static final Random RANDOM = new Random();
  public static final int MAX_ID_LENGTH = 64;

  private final DatabaseMetaData _sourceDatabaseMetaData;
  private final int _maxIdLength;
  private final DatabaseMetaData _targetDatabaseMetaData;
  private SchemaColumnTypeMapper _columnTypeMapper = new DefaultSchemaColumnTypeMapper();
  private ColumnMapper _columnNameMapper = new DefaultColumnMapper(CaseConversionMode.UPPER);
  private TableMapper _tableMapper = new DefaultTableMapper(CaseConversionMode.UPPER);

  public DatabaseSchemaScriptCreator(
    final DatabaseMetaData sourceDatabaseMetaData,
    final DatabaseMetaData targetDatabaseMetaData,
    final int maxIdLength) {
    _targetDatabaseMetaData = targetDatabaseMetaData;
    assert sourceDatabaseMetaData != null : "sourceDatabaseMetaData != null";
    assert targetDatabaseMetaData != null : "targetDatabaseMetaData != null";

    _sourceDatabaseMetaData = sourceDatabaseMetaData;
    _maxIdLength = maxIdLength;
  }

  public DatabaseSchemaScriptCreator(final DatabaseMetaData databaseMetaData, final DatabaseMetaData targetDatabaseMetaData) {
    this(databaseMetaData, targetDatabaseMetaData, MAX_ID_LENGTH);
  }

  public DatabaseSchemaScriptCreator(final DatabaseMetaData databaseMetaData) {
    this(databaseMetaData, databaseMetaData);
  }

  public void setTableMapper(final TableMapper tableMapper) {
    _tableMapper = tableMapper;
  }

  public void setColumnMapper(final ColumnMapper columnNameMapper) {
    assert columnNameMapper != null : "columnNameMapper != null";
    _columnNameMapper = columnNameMapper;
  }

  public void setColumnTypeMapper(final SchemaColumnTypeMapper columnTypeMapper) {
    assert columnTypeMapper != null : "columnTypeMapper != null";
    _columnTypeMapper = columnTypeMapper;
  }

  public List<String> createTableStatements() throws SQLException {
    final List<TableMetaData> tables = new TableOrderTool().getOrderedTables(_sourceDatabaseMetaData.getTableMetaData(), true);
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
    final List<TableMetaData> tables = _sourceDatabaseMetaData.getTableMetaData();
    return createPrimaryKeyStatements(tables);
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
    final List<TableMetaData> tables = _sourceDatabaseMetaData.getTableMetaData();
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
    final List<TableMetaData> tables = _sourceDatabaseMetaData.getTableMetaData();
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
    final StringBuilder builder = new StringBuilder("CREATE TABLE "
      + _tableMapper.fullyQualifiedTableName(tableMetaData, _targetDatabaseMetaData)
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
    final String tableName = _tableMapper.mapTableName(tableMetaData, _targetDatabaseMetaData);
    final String qualifiedTableName = _tableMapper.fullyQualifiedTableName(tableMetaData, _targetDatabaseMetaData);
    final String pkName = "PK_" + tableName + "_" + counter;
    final StringBuilder builder = new StringBuilder("ALTER TABLE " + qualifiedTableName
      + " ADD CONSTRAINT " + pkName + " PRIMARY KEY (");

    for (final ColumnMetaData columnMetaData : primaryKeyColumns) {
      builder.append(_columnNameMapper.mapColumnName(columnMetaData, tableMetaData)).append(", ");
    }

    builder.setLength(builder.length() - 2);
    builder.append(");");
    return builder.toString();
  }

  private String createIndex(final IndexMetaData indexMetaData, final int counter) throws SQLException {
    final TableMetaData tableMetaData = indexMetaData.getTableMetaData();
    final String indexName = createConstraintName("IDX_", CaseConversionMode.UPPER.convert(indexMetaData.getIndexName())
        + "_"
        + _tableMapper.mapTableName(tableMetaData, _targetDatabaseMetaData)
        + "_",
      counter);
    return createIndex(indexMetaData, indexName);
  }

  public String createIndex(final IndexMetaData indexMetaData) throws SQLException {
    return createIndex(indexMetaData, indexMetaData.getIndexName());
  }

  private String createIndex(final IndexMetaData indexMetaData, final String indexName) throws SQLException {
    final TableMetaData tableMetaData = indexMetaData.getTableMetaData();
    final String unique = indexMetaData.isUnique() ? " UNIQUE " : " ";

    final StringBuilder builder = new StringBuilder("CREATE" + unique
      + "INDEX "
      + indexName
      + " ON "
      + _tableMapper.fullyQualifiedTableName(tableMetaData, _targetDatabaseMetaData)
      + "(");

    for (final Iterator<ColumnMetaData> iterator = indexMetaData.getColumnMetaData().iterator(); iterator.hasNext(); ) {
      final ColumnMetaData columnMetaData = iterator.next();

      builder.append(_columnNameMapper.mapColumnName(columnMetaData, tableMetaData));

      if (iterator.hasNext()) {
        builder.append(", ");
      }
    }

    builder.append(");");
    return builder.toString();
  }

  private String createForeignKey(final ColumnMetaData referencingColumn, final int counter) throws SQLException {
    final TableMetaData tableMetaData = referencingColumn.getTableMetaData();
    final ColumnMetaData referencedColumn = referencingColumn.getReferencedColumn();
    final String tablename = _tableMapper.mapTableName(tableMetaData, _targetDatabaseMetaData);
    final String fkName = createConstraintName("FK_", tablename + "_"
      + _columnNameMapper.mapColumnName(referencingColumn, referencedColumn.getTableMetaData())
      + "_"
      + _columnNameMapper.mapColumnName(referencedColumn, referencedColumn.getTableMetaData())
      + "_", counter);

    return createForeignKey(referencingColumn, fkName);
  }

  public String createForeignKey(final ColumnMetaData referencingColumn, final String fkName) throws SQLException {
    final TableMetaData tableMetaData = referencingColumn.getTableMetaData();
    final ColumnMetaData referencedColumn = referencingColumn.getReferencedColumn();
    final String qualifiedTableName = _tableMapper.fullyQualifiedTableName(tableMetaData, _targetDatabaseMetaData);

    return "ALTER TABLE " + qualifiedTableName + " ADD CONSTRAINT " + fkName +
      " FOREIGN KEY (" + _columnNameMapper.mapColumnName(referencingColumn, referencingColumn.getTableMetaData())
      + ") REFERENCES "
      + _tableMapper.fullyQualifiedTableName(referencedColumn.getTableMetaData(), _targetDatabaseMetaData)
      + "(" + _columnNameMapper.mapColumnName(referencedColumn, referencedColumn.getTableMetaData()) + ");";
  }

  public String createForeignKey(final ForeignKeyMetaData foreignKeyMetaData) throws SQLException {
    final ColumnMetaData referencingColumn = foreignKeyMetaData.getReferencingColumn();
    final TableMetaData tableMetaData = referencingColumn.getTableMetaData();
    final ColumnMetaData referencedColumn = foreignKeyMetaData.getReferencedColumn();
    final String qualifiedTableName = _tableMapper.fullyQualifiedTableName(tableMetaData, _targetDatabaseMetaData);

    return "ALTER TABLE " + qualifiedTableName + " ADD CONSTRAINT " + foreignKeyMetaData.getForeignKeyName() +
      " FOREIGN KEY (" + _columnNameMapper.mapColumnName(referencingColumn, referencingColumn.getTableMetaData()) + ") REFERENCES "
      + _tableMapper.fullyQualifiedTableName(referencedColumn.getTableMetaData(), _targetDatabaseMetaData)
      + "(" + _columnNameMapper.mapColumnName(referencedColumn, referencedColumn.getTableMetaData()) + ");";
  }

  private String createColumn(final ColumnMetaData columnMetaData) throws SQLException {
    final StringBuilder builder = new StringBuilder();

    builder.append(_columnNameMapper.mapColumnName(columnMetaData, columnMetaData.getTableMetaData()))
      .append(" ").append(_columnTypeMapper.getColumnType(columnMetaData));

    if (!columnMetaData.isNullable()) {
      builder.append(" NOT NULL");
    }

    return builder.toString();
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
