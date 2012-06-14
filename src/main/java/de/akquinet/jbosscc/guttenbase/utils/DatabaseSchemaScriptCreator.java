package de.akquinet.jbosscc.guttenbase.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Create DDL script from given database meta data.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DatabaseSchemaScriptCreator {
	private int _foreignKeyCounter = 1;

	public List<String> createTableStatements(final DatabaseMetaData databaseMetaData) {
		final List<String> result = new ArrayList<String>();

		for (final TableMetaData tableMetaData : databaseMetaData.getTableMetaData()) {
			result.add(createTable(tableMetaData));
		}

		return result;
	}

	public List<String> createIndexStatements(final DatabaseMetaData databaseMetaData) {
		final List<String> result = new ArrayList<String>();

		for (final TableMetaData tableMetaData : databaseMetaData.getTableMetaData()) {
			final List<IndexMetaData> indexes = tableMetaData.getIndexes();

			for (final IndexMetaData indexMetaData : indexes) {
				result.add(createIndexesForTable(indexMetaData));
			}
		}

		return result;
	}

	public List<String> createForeignKeyStatements(final DatabaseMetaData databaseMetaData) {
		final List<String> result = new ArrayList<String>();

		for (final TableMetaData tableMetaData : databaseMetaData.getTableMetaData()) {
			final List<ColumnMetaData> columns = tableMetaData.getColumnMetaData();

			for (final ColumnMetaData columnMetaData : columns) {
				if (columnMetaData.getReferencedColumn() != null) {
					result.add(createForeignKeyForTable(columnMetaData));
				}
			}
		}

		return result;
	}

	private String createTable(final TableMetaData tableMetaData) {
		final String schema = tableMetaData.getDatabaseMetaData().getSchema().trim();
		final StringBuilder builder = new StringBuilder("CREATE TABLE " + ("".equals(schema) ? "" : schema + ".")
				+ tableMetaData.getTableName() + "\n(\n");

		for (final Iterator<ColumnMetaData> iterator = tableMetaData.getColumnMetaData().iterator(); iterator.hasNext();) {
			final ColumnMetaData columnMetaData = iterator.next();

			builder.append("  " + createColumn(columnMetaData));

			if (iterator.hasNext()) {
				builder.append(", \n");
			}
		}

		builder.append("\n);");
		return builder.toString();
	}

	private String createIndexesForTable(final IndexMetaData indexMetaData) {
		final TableMetaData tableMetaData = indexMetaData.getTableMetaData();
		final String schema = tableMetaData.getDatabaseMetaData().getSchema().trim();
		final String schemaPrefix = "".equals(schema) ? "" : schema + ".";
		final String unique = indexMetaData.isUnique() ? " UNIQUE " : " ";

		final StringBuilder builder = new StringBuilder("CREATE" + unique + "INDEX " + indexMetaData.getIndexName() + " ON " + schemaPrefix
				+ tableMetaData.getTableName() + "(");

		for (final Iterator<ColumnMetaData> iterator = indexMetaData.getColumnMetaData().iterator(); iterator.hasNext();) {
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
		final String schema = tableMetaData.getDatabaseMetaData().getSchema().trim();
		final String schemaPrefix = "".equals(schema) ? "" : schema + ".";
		final ColumnMetaData referencedColumn = columnMetaData.getReferencedColumn();

		final StringBuilder builder = new StringBuilder("ALTER TABLE " + schemaPrefix + tableMetaData.getTableName() + " ADD CONSTRAINT ");
		builder.append("FK_" + columnMetaData.getColumnName().toUpperCase() + "_" + referencedColumn.getColumnName().toUpperCase() + "_"
				+ _foreignKeyCounter++);
		builder.append(" FOREIGN KEY (" + columnMetaData.getColumnName() + ") + REFERENCES " + schemaPrefix
				+ referencedColumn.getTableMetaData() + "(" + referencedColumn.getColumnName() + ")");
		return builder.toString();
	}

	private String createColumn(final ColumnMetaData columnMetaData) {
		final StringBuilder builder = new StringBuilder();

		builder.append(columnMetaData.getColumnName() + " " + columnMetaData.getColumnTypeName());

		if (columnMetaData.isPrimaryKey()) {
			builder.append(" PRIMARY KEY");
		} else if (!columnMetaData.isNullable()) {
			builder.append(" NOT NULL");
		}

		return builder.toString();
	}
}
