package de.akquinet.jbosscc.guttenbase.tools.schema;

import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.meta.*;
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
public class DatabaseSchemaScriptCreator {
  private static final Random RANDOM = new Random();
  public static final int MAX_ID_LENGTH = 64;

  private final DatabaseMetaData _sourceDatabaseMetaData;
  private final String _targetSchema;
  private SchemaColumnTypeMapper _columnTypeMapper = new DefaultSchemaColumnTypeMapper();
  private final CaseConversionMode _caseConversionMode;
  private final int _maxIdLength;

  public DatabaseSchemaScriptCreator(
          final DatabaseMetaData databaseMetaData,
          final String targetSchema,
          final CaseConversionMode caseConversionMode,
          final int maxIdLength) {
    assert databaseMetaData != null : "databaseMetaData != null";
    assert targetSchema != null : "schema != null";
    assert caseConversionMode != null : "caseConversionMode != null";

    _sourceDatabaseMetaData = databaseMetaData;
    _targetSchema = targetSchema;
    _caseConversionMode = caseConversionMode;
    _maxIdLength = maxIdLength;
  }

  public DatabaseSchemaScriptCreator(final DatabaseMetaData databaseMetaData, final String schema) {
    this(databaseMetaData, schema, CaseConversionMode.NONE, MAX_ID_LENGTH);
  }

  public DatabaseSchemaScriptCreator(final DatabaseMetaData databaseMetaData) {
    this(databaseMetaData, databaseMetaData.getSchema().trim());
  }

  public void setColumnTypeMapper(final SchemaColumnTypeMapper columnTypeMapper) {
    assert columnTypeMapper != null : "columnTypeMapper != null";
    _columnTypeMapper = columnTypeMapper;
  }

  public List<String> createTableStatements() throws SQLException {
    final List<TableMetaData> tables = new TableOrderTool().getOrderedTables(_sourceDatabaseMetaData.getTableMetaData(), true);
    return createTableStatements(tables);
  }

  public List<String> createTableStatements(final List<TableMetaData> tables) {
    final List<String> result = new ArrayList<String>();

    for (final TableMetaData tableMetaData : tables) {
      result.add(createTable(tableMetaData));
    }

    return result;
  }

  public List<String> createPrimaryKeyStatements() throws SQLException {
    final List<TableMetaData> tables = _sourceDatabaseMetaData.getTableMetaData();
    return createPrimaryKeyStatements(tables);
  }

  public List<String> createPrimaryKeyStatements(final List<TableMetaData> tables) {
    final List<String> result = new ArrayList<String>();

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

  public List<String> createIndexStatements(final List<TableMetaData> tables) {
    final List<String> result = new ArrayList<String>();

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

  public List<String> createForeignKeyStatements(final List<TableMetaData> tables) {
    final List<String> result = new ArrayList<String>();

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

  public String createTable(final TableMetaData tableMetaData) {
    final StringBuilder builder = new StringBuilder("CREATE TABLE " + ("".equals(_targetSchema) ? "" : _targetSchema + ".")
            + _caseConversionMode.convert(tableMetaData.getTableName())
            + "\n(\n");

    for (final Iterator<ColumnMetaData> iterator = tableMetaData.getColumnMetaData().iterator(); iterator.hasNext(); ) {
      final ColumnMetaData columnMetaData = iterator.next();

      builder.append("  " + createColumn(columnMetaData));

      if (iterator.hasNext()) {
        builder.append(", \n");
      }
    }

    builder.append("\n);");
    return builder.toString();
  }

  private String createPrimaryKeyStatement(final TableMetaData tableMetaData, final List<ColumnMetaData> primaryKeyColumns,
                                           final int counter) {
    final String tableName = _caseConversionMode.convert(tableMetaData.getTableName());
    final String pkName = "PK_" + tableName + "_" + counter;
    final StringBuilder builder = new StringBuilder("ALTER TABLE " + ("".equals(_targetSchema) ? "" : _targetSchema + ".")
            + tableName
            + " ADD CONSTRAINT " +
            pkName
            + " PRIMARY KEY (");

    for (final ColumnMetaData columnMetaData : primaryKeyColumns) {
      builder.append(_caseConversionMode.convert(columnMetaData.getColumnName()) + ", ");
    }

    builder.setLength(builder.length() - 2);
    builder.append(");");
    return builder.toString();
  }

  private String createIndex(final IndexMetaData indexMetaData, final int counter) {
    final TableMetaData tableMetaData = indexMetaData.getTableMetaData();
    final String indexName = createConstraintName("IDX_", _caseConversionMode.convert(indexMetaData.getIndexName())
                    + "_"
                    + _caseConversionMode.convert(tableMetaData.getTableName())
                    + "_",
            counter);
    return createIndex(indexMetaData, indexName);
  }

  public String createIndex(final IndexMetaData indexMetaData) {
    return createIndex(indexMetaData, indexMetaData.getIndexName());
  }

  private String createIndex(final IndexMetaData indexMetaData, final String indexName) {
    final TableMetaData tableMetaData = indexMetaData.getTableMetaData();
    final String schemaPrefix = "".equals(_targetSchema) ? "" : _targetSchema + ".";
    final String unique = indexMetaData.isUnique() ? " UNIQUE " : " ";

    final StringBuilder builder = new StringBuilder("CREATE" + unique
            + "INDEX "
            + indexName
            + " ON "
            + schemaPrefix
            + _caseConversionMode.convert(tableMetaData.getTableName())
            + "(");

    for (final Iterator<ColumnMetaData> iterator = indexMetaData.getColumnMetaData().iterator(); iterator.hasNext(); ) {
      final ColumnMetaData columnMetaData = iterator.next();

      builder.append(_caseConversionMode.convert(columnMetaData.getColumnName()));

      if (iterator.hasNext()) {
        builder.append(", ");
      }
    }

    builder.append(");");
    return builder.toString();
  }

  private String createForeignKey(final ColumnMetaData referencingColumn, final int counter) {
    final TableMetaData tableMetaData = referencingColumn.getTableMetaData();
    final ColumnMetaData referencedColumn = referencingColumn.getReferencedColumn();
    final String tablename = _caseConversionMode.convert(tableMetaData.getTableName());
    final String fkName = createConstraintName("FK_", tablename + "_"
            + _caseConversionMode.convert(referencingColumn.getColumnName())
            + "_"
            + _caseConversionMode.convert(referencedColumn.getColumnName()) + "_", counter);

    return createForeignKey(referencingColumn, fkName);
  }

  public String createForeignKey(final ColumnMetaData referencingColumn, final String fkName) {
    final TableMetaData tableMetaData = referencingColumn.getTableMetaData();
    final ColumnMetaData referencedColumn = referencingColumn.getReferencedColumn();
    final String tablename = _caseConversionMode.convert(tableMetaData.getTableName());
    final String schemaPrefix = "".equals(_targetSchema) ? "" : _targetSchema + ".";

    final StringBuilder builder = new StringBuilder("ALTER TABLE " + schemaPrefix + tablename + " ADD CONSTRAINT ");
    builder.append(fkName);
    builder.append(" FOREIGN KEY (" + _caseConversionMode.convert(referencingColumn.getColumnName())
            + ") REFERENCES "
            + schemaPrefix
            + _caseConversionMode.convert(referencedColumn.getTableMetaData().getTableName())
            + "("
            + _caseConversionMode.convert(referencedColumn.getColumnName())
            + ");");

    return builder.toString();
  }

  public String createForeignKey(final ForeignKeyMetaData foreignKeyMetaData) {
    final ColumnMetaData referencingColumn = foreignKeyMetaData.getReferencingColumn();
    final TableMetaData tableMetaData = referencingColumn.getTableMetaData();
    final ColumnMetaData referencedColumn = foreignKeyMetaData.getReferencedColumn();
    final String tablename = _caseConversionMode.convert(tableMetaData.getTableName());
    final String schemaPrefix = "".equals(_targetSchema) ? "" : _targetSchema + ".";

    final StringBuilder builder = new StringBuilder("ALTER TABLE " + schemaPrefix + tablename + " ADD CONSTRAINT ");
    builder.append(foreignKeyMetaData.getForeignKeyName());
    builder.append(" FOREIGN KEY (" + _caseConversionMode.convert(referencingColumn.getColumnName())
            + ") REFERENCES "
            + schemaPrefix
            + _caseConversionMode.convert(referencedColumn.getTableMetaData().getTableName())
            + "("
            + _caseConversionMode.convert(referencedColumn.getColumnName())
            + ");");

    return builder.toString();
  }

  private String createColumn(final ColumnMetaData columnMetaData) {
    final StringBuilder builder = new StringBuilder();

    builder.append(_caseConversionMode.convert(columnMetaData.getColumnName()) + " "
            + _columnTypeMapper.getColumnType(columnMetaData));

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
