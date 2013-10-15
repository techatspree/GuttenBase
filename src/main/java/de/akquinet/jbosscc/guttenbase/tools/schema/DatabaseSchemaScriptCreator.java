package de.akquinet.jbosscc.guttenbase.tools.schema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Create DDL script from given database meta data.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DatabaseSchemaScriptCreator {
	private int _keyCounter = 1;

	private final DatabaseMetaData _sourceDatabaseMetaData;
	private final String _targetSchema;

	private SchemaColumnTypeMapper _columnTypeMapper = new DefaultSchemaColumnTypeMapper();

	public DatabaseSchemaScriptCreator(final DatabaseMetaData databaseMetaData,
			final String schema) {
		assert databaseMetaData != null : "databaseMetaData != null";
		assert schema != null : "schema != null";

		_sourceDatabaseMetaData = databaseMetaData;
		_targetSchema = schema;
	}

	public DatabaseSchemaScriptCreator(final DatabaseMetaData databaseMetaData) {
		this(databaseMetaData, databaseMetaData.getSchema().trim());
	}

	public void setColumnTypeMapper(SchemaColumnTypeMapper columnTypeMapper) {
		assert columnTypeMapper != null : "columnTypeMapper != null";
		_columnTypeMapper = columnTypeMapper;
	}

	public List<String> createTableStatements() {
		final List<String> result = new ArrayList<String>();

		for (final TableMetaData tableMetaData : _sourceDatabaseMetaData
				.getTableMetaData()) {
			result.add(createTable(tableMetaData));
		}

		return result;
	}

	public List<String> createPrimaryKeyStatements() {
		final List<String> result = new ArrayList<String>();

		for (final TableMetaData tableMetaData : _sourceDatabaseMetaData
				.getTableMetaData()) {
			final List<ColumnMetaData> primaryKeyColumns = tableMetaData
					.getPrimaryKeyColumns();

			if (!primaryKeyColumns.isEmpty()) {
				result.add(createPrimaryKeyStatement(tableMetaData,
						primaryKeyColumns));
			}
		}

		return result;
	}

	public List<String> createIndexStatements() {
		final List<String> result = new ArrayList<String>();

		for (final TableMetaData tableMetaData : _sourceDatabaseMetaData
				.getTableMetaData()) {
			final List<IndexMetaData> indexes = tableMetaData.getIndexes();

			for (final IndexMetaData indexMetaData : indexes) {
				result.add(createIndexesForTable(indexMetaData));
			}
		}

		return result;
	}

	public List<String> createForeignKeyStatements() {
		final List<String> result = new ArrayList<String>();

		for (final TableMetaData tableMetaData : _sourceDatabaseMetaData
				.getTableMetaData()) {
			final List<ColumnMetaData> columns = tableMetaData
					.getColumnMetaData();

			for (final ColumnMetaData columnMetaData : columns) {
				if (columnMetaData.getReferencedColumn() != null) {
					result.add(createForeignKeyForTable(columnMetaData));
				}
			}
		}

		return result;
	}

	private String createTable(final TableMetaData tableMetaData) {
		final StringBuilder builder = new StringBuilder("CREATE TABLE "
				+ ("".equals(_targetSchema) ? "" : _targetSchema + ".")
				+ tableMetaData.getTableName() + "\n(\n");

		for (final Iterator<ColumnMetaData> iterator = tableMetaData
				.getColumnMetaData().iterator(); iterator.hasNext();) {
			final ColumnMetaData columnMetaData = iterator.next();

			builder.append("  " + createColumn(columnMetaData));

			if (iterator.hasNext()) {
				builder.append(", \n");
			}
		}

		builder.append("\n);");
		return builder.toString();
	}

	private String createPrimaryKeyStatement(final TableMetaData tableMetaData,
			final List<ColumnMetaData> primaryKeyColumns) {
		final StringBuilder builder = new StringBuilder("ALTER TABLE "
				+ ("".equals(_targetSchema) ? "" : _targetSchema + ".")
				+ tableMetaData.getTableName() + " ADD CONSTRAINT PK_"
				+ tableMetaData.getTableName() + "_" + _keyCounter++
				+ " PRIMARY KEY (");

		for (final ColumnMetaData columnMetaData : primaryKeyColumns) {
			builder.append(columnMetaData.getColumnName() + ", ");
		}

		builder.setLength(builder.length() - 2);
		builder.append(");");
		return builder.toString();
	}

	private String createIndexesForTable(final IndexMetaData indexMetaData) {
		final TableMetaData tableMetaData = indexMetaData.getTableMetaData();
		final String schemaPrefix = "".equals(_targetSchema) ? "" : _targetSchema + ".";
		final String unique = indexMetaData.isUnique() ? " UNIQUE " : " ";

		final StringBuilder builder = new StringBuilder("CREATE" + unique
				+ "INDEX " + indexMetaData.getIndexName() + "_" + _keyCounter++
				+ " ON " + schemaPrefix + tableMetaData.getTableName() + "(");

		for (final Iterator<ColumnMetaData> iterator = indexMetaData
				.getColumnMetaData().iterator(); iterator.hasNext();) {
			final ColumnMetaData columnMetaData = iterator.next();

			builder.append(columnMetaData.getColumnName());

			if (iterator.hasNext()) {
				builder.append(", ");
			}
		}

		builder.append(");");
		return builder.toString();
	}

	private String createForeignKeyForTable(final ColumnMetaData columnMetaData) {
		final TableMetaData tableMetaData = columnMetaData.getTableMetaData();
		final String schemaPrefix = "".equals(_targetSchema) ? "" : _targetSchema + ".";
		final ColumnMetaData referencedColumn = columnMetaData
				.getReferencedColumn();

		final StringBuilder builder = new StringBuilder("ALTER TABLE "
				+ schemaPrefix + tableMetaData.getTableName()
				+ " ADD CONSTRAINT ");
		builder.append("FK_" + columnMetaData.getColumnName().toUpperCase()
				+ "_" + referencedColumn.getColumnName().toUpperCase() + "_"
				+ _keyCounter++);
		builder.append(" FOREIGN KEY (" + columnMetaData.getColumnName()
				+ ") REFERENCES " + schemaPrefix
				+ referencedColumn.getTableMetaData() + "("
				+ referencedColumn.getColumnName() + ");");
		return builder.toString();
	}

	private String createColumn(final ColumnMetaData columnMetaData) {
		final StringBuilder builder = new StringBuilder();

		builder.append(columnMetaData.getColumnName() + " "
				+ _columnTypeMapper.getColumnType(columnMetaData));

		if (!columnMetaData.isNullable()) {
			builder.append(" NOT NULL");
		}

		return builder.toString();
	}
}
