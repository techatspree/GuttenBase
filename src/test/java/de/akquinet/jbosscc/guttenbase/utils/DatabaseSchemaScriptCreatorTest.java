package de.akquinet.jbosscc.guttenbase.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.builder.ColumnMetaDataBuilder;
import de.akquinet.jbosscc.guttenbase.meta.builder.DatabaseMetaDataBuilder;
import de.akquinet.jbosscc.guttenbase.meta.builder.ForeignKeyMetaDataBuilder;
import de.akquinet.jbosscc.guttenbase.meta.builder.IndexMetaDataBuilder;
import de.akquinet.jbosscc.guttenbase.meta.builder.TableMetaDataBuilder;

public class DatabaseSchemaScriptCreatorTest
{
  private final DatabaseMetaData _databaseMetaData = createDatabaseMetaData();
  private final DatabaseSchemaScriptCreator _objectUnderTest = new DatabaseSchemaScriptCreator(_databaseMetaData);

  private DatabaseMetaData createDatabaseMetaData()
  {
    final DatabaseMetaDataBuilder databaseMetaDataBuilder = new DatabaseMetaDataBuilder();
    final TableMetaDataBuilder tableBuilder1 = createTable(1, databaseMetaDataBuilder);
    final TableMetaDataBuilder tableBuilder2 = createTable(2, databaseMetaDataBuilder);

    final ForeignKeyMetaDataBuilder foreignKeyMetaDataBuilder1 = new ForeignKeyMetaDataBuilder(tableBuilder1)
        .setForeignKeyName("FK_NAME").setReferencingColumn(tableBuilder1.getColumn("NAME"))
        .setReferencedColumn(tableBuilder2.getColumn("NAME"));
    final ForeignKeyMetaDataBuilder foreignKeyMetaDataBuilder2 = new ForeignKeyMetaDataBuilder(tableBuilder2)
        .setForeignKeyName("FK_NAME").setReferencingColumn(tableBuilder1.getColumn("NAME"))
        .setReferencedColumn(tableBuilder2.getColumn("NAME"));

    tableBuilder1.addImportedForeignKey(foreignKeyMetaDataBuilder1);
    tableBuilder2.addExportedForeignKey(foreignKeyMetaDataBuilder2);

    databaseMetaDataBuilder.addTable(tableBuilder1);
    databaseMetaDataBuilder.addTable(tableBuilder2);
    databaseMetaDataBuilder.setSchema("schemaName");

    return databaseMetaDataBuilder.build();
  }

  private TableMetaDataBuilder createTable(final int index, final DatabaseMetaDataBuilder databaseMetaDataBuilder)
  {
    final TableMetaDataBuilder tableMetaDataBuilder = new TableMetaDataBuilder(databaseMetaDataBuilder)
        .setTableName("MY_TABLE" + index);
    final ColumnMetaDataBuilder primaryKeyBuilder = new ColumnMetaDataBuilder(tableMetaDataBuilder).setColumnName("ID")
        .setColumnTypeName("BIGINT").setNullable(false).setPrimaryKey(true);
    final ColumnMetaDataBuilder nameBuilder = new ColumnMetaDataBuilder(tableMetaDataBuilder).setColumnName("NAME")
        .setColumnTypeName("VARCHAR(100)").setNullable(false);

    tableMetaDataBuilder
        .addColumn(primaryKeyBuilder)
        .addColumn(nameBuilder)
        .addIndex(
            new IndexMetaDataBuilder(tableMetaDataBuilder).setAscending(true).setIndexName("NAME_IDX" + index).setUnique(true)
                .addColumn(nameBuilder));
    return tableMetaDataBuilder;
  }

  @Test
  public void testDDL() throws Exception
  {
    final List<String> tableStatements = _objectUnderTest.createTableStatements();
    assertEquals(2, tableStatements.size());

    final String createStatement = tableStatements.get(0);

    assertTrue(createStatement, createStatement.startsWith("CREATE TABLE schemaName.MY_TABLE1"));
    assertTrue(createStatement, createStatement.contains("ID BIGINT NOT NULL"));
    assertTrue(createStatement, createStatement.contains("NAME VARCHAR(100) NOT NULL"));

    final List<String> indexStatements = _objectUnderTest.createIndexStatements();
    assertEquals(2, indexStatements.size());
    final String indexStatement = indexStatements.get(0);
    assertTrue(indexStatement, indexStatement.startsWith("CREATE UNIQUE INDEX NAME_IDX1_1 ON schemaName.MY_TABLE"));
    assertTrue(indexStatement, indexStatement.contains("NAME"));

    final List<String> foreignKeyStatements = _objectUnderTest.createForeignKeyStatements();
    assertEquals(1, foreignKeyStatements.size());
    final String foreignKeyStatement = foreignKeyStatements.get(0);

    assertTrue(foreignKeyStatement, foreignKeyStatement.startsWith("ALTER TABLE schemaName.MY_TABLE1"));
  }
}
