package de.akquinet.jbosscc.guttenbase.utils;

import de.akquinet.jbosscc.guttenbase.configuration.EasymockConnectionInfo;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultColumnMapper;
import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultTableMapper;
import de.akquinet.jbosscc.guttenbase.exceptions.IncompatibleColumnsException;
import de.akquinet.jbosscc.guttenbase.exceptions.IncompatibleTablesException;
import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.hints.ColumnMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.ColumnTypeMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.TableMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.DefaultColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.meta.builder.ColumnMetaDataBuilder;
import de.akquinet.jbosscc.guttenbase.meta.builder.DatabaseMetaDataBuilder;
import de.akquinet.jbosscc.guttenbase.meta.builder.ForeignKeyMetaDataBuilder;
import de.akquinet.jbosscc.guttenbase.meta.builder.IndexMetaDataBuilder;
import de.akquinet.jbosscc.guttenbase.meta.builder.TableMetaDataBuilder;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.tools.schema.SchemaScriptCreatorTool;
import org.junit.Test;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatabaseSchemaScriptCreatorTest {
  public static final String SOURCE = "source";
  public static final String TARGET = "target";

  private final DatabaseMetaData _databaseMetaData = createDatabaseMetaData().build();

  private final ConnectorRepository _connectorRepository = createRepository();

  private ConnectorRepository createRepository() {
    final ConnectorRepository repository = new ConnectorRepositoryImpl() {
      @Override
      public DatabaseMetaData getDatabaseMetaData(final String connectorId) throws SQLException {
        return _databaseMetaData;
      }
    };

    repository.addConnectionInfo(SOURCE, new EasymockConnectionInfo());
    repository.addConnectionInfo(TARGET, new EasymockConnectionInfo());

    repository.addConnectorHint(TARGET, new TableMapperHint() {

      @Override
      public TableMapper getValue() {
        return new DefaultTableMapper(CaseConversionMode.UPPER);
      }
    });
    repository.addConnectorHint(TARGET, new ColumnMapperHint() {

      @Override
      public ColumnMapper getValue() {
        return new DefaultColumnMapper(CaseConversionMode.UPPER);
      }
    });

    return repository;
  }

  private final SchemaScriptCreatorTool _objectUnderTest = new SchemaScriptCreatorTool(_connectorRepository, SOURCE, TARGET);

  private DatabaseMetaDataBuilder createDatabaseMetaData() {
    final DatabaseMetaDataBuilder databaseMetaDataBuilder = new DatabaseMetaDataBuilder();
    final TableMetaDataBuilder tableBuilder1 = createTable(1, databaseMetaDataBuilder);
    final TableMetaDataBuilder tableBuilder2 = createTable(2, databaseMetaDataBuilder);

    final ForeignKeyMetaDataBuilder foreignKeyMetaDataBuilder1 = new ForeignKeyMetaDataBuilder(tableBuilder1)
      .setForeignKeyName("FK_Name").setReferencingColumn(tableBuilder1.getColumn("Name"))
      .setReferencedColumn(tableBuilder2.getColumn("Name"));
    final ForeignKeyMetaDataBuilder foreignKeyMetaDataBuilder2 = new ForeignKeyMetaDataBuilder(tableBuilder2)
      .setForeignKeyName("FK_Name").setReferencingColumn(tableBuilder1.getColumn("Name"))
      .setReferencedColumn(tableBuilder2.getColumn("Name"));

    tableBuilder1.addImportedForeignKey(foreignKeyMetaDataBuilder1);
    tableBuilder2.addExportedForeignKey(foreignKeyMetaDataBuilder2);

    return databaseMetaDataBuilder.addTable(tableBuilder1).addTable(tableBuilder2)
      .setSchema("schemaName").addDatabaseProperty("getMaxColumnNameLength", 42)
      .addDatabaseProperty("getDatabaseProductName", "GuttenBaseDB");
  }

  private TableMetaDataBuilder createTable(final int index, final DatabaseMetaDataBuilder databaseMetaDataBuilder) {
    final TableMetaDataBuilder tableMetaDataBuilder = new TableMetaDataBuilder(databaseMetaDataBuilder)
      .setTableName("My_Table" + index);
    final ColumnMetaDataBuilder primaryKeyBuilder = new ColumnMetaDataBuilder(tableMetaDataBuilder).setColumnName("Id")
      .setColumnTypeName("BIGINT").setNullable(false).setPrimaryKey(true);
    final ColumnMetaDataBuilder nameBuilder = new ColumnMetaDataBuilder(tableMetaDataBuilder).setColumnName("Name")
      .setColumnTypeName("VARCHAR(100)").setNullable(false);

    tableMetaDataBuilder
      .addColumn(primaryKeyBuilder)
      .addColumn(nameBuilder)
      .addIndex(
        new IndexMetaDataBuilder(tableMetaDataBuilder).setAscending(true).setIndexName("Name_IDX" + index).setUnique(true)
          .addColumn(nameBuilder));
    return tableMetaDataBuilder;
  }

  @Test
  public void testMetaData() throws Exception {
    assertEquals("GuttenBaseDB", _databaseMetaData.getDatabaseMetaData().getDatabaseProductName());
    assertEquals(42, _databaseMetaData.getDatabaseMetaData().getMaxColumnNameLength());
  }

  @Test(expected = IncompatibleTablesException.class)
  public void testTableNameLength() throws Exception {
    final DatabaseMetaDataBuilder metaDataBuilder = createDatabaseMetaData();
    final TableMetaData table = createTable(3, metaDataBuilder)
      .setTableName("SomeTableNameThatIsMuchLongerThanFortyTwoCharactersSupportedByDB").build();

    _objectUnderTest.createTable(table);
  }

  @Test(expected = IncompatibleColumnsException.class)
  public void testColumnNameLength() throws Exception {
    final DatabaseMetaDataBuilder metaDataBuilder = createDatabaseMetaData();
    final TableMetaDataBuilder tableBuilder = createTable(3, metaDataBuilder);
    final ColumnMetaData columnMetaData = new ColumnMetaDataBuilder(tableBuilder)
      .setColumnName("SomeColumnNameThatIsMuchLongerThanFortyTwoCharactersSupportedByDB").build();

    _objectUnderTest.createColumn(columnMetaData);
  }

  @Test
  public void testDDL() throws Exception {
    final List<String> tableStatements = _objectUnderTest.createTableStatements();
    assertEquals(2, tableStatements.size());

    final String createStatement = tableStatements.get(0);

    assertTrue(createStatement, createStatement.startsWith("CREATE TABLE schemaName.MY_TABLE"));
    assertTrue(createStatement, createStatement.contains("ID BIGINT NOT NULL"));
    assertTrue(createStatement, createStatement.contains("NAME VARCHAR(100) NOT NULL"));

    final List<String> indexStatements = _objectUnderTest.createIndexStatements();
    assertEquals(2, indexStatements.size());
    final String indexStatement = indexStatements.get(0);
    assertTrue(indexStatement, indexStatement.startsWith("CREATE UNIQUE INDEX IDX_NAME_IDX2_MY_TABLE2_1 ON schemaName.MY_TABLE"));
    assertTrue(indexStatement, indexStatement.contains("NAME"));

    final List<String> foreignKeyStatements = _objectUnderTest.createForeignKeyStatements();
    assertEquals(1, foreignKeyStatements.size());
    final String foreignKeyStatement = foreignKeyStatements.get(0).toUpperCase();

    assertTrue(foreignKeyStatement, foreignKeyStatement.startsWith("ALTER TABLE SCHEMANAME.MY_TABLE1 ADD CONSTRAINT FK_MY_TABLE1_NAME_NAME_1 FOREIGN KEY"));
  }

  @Test
  public void testSchemaColumnTypeMapper() throws Exception {
    _connectorRepository.addConnectorHint(TARGET, new ColumnTypeMapperHint() {
      @Override
      public ColumnTypeMapper getValue() {
        return new DefaultColumnTypeMapper()
          .addMapping(DatabaseType.GENERIC, DatabaseType.GENERIC, "BIGINT", "INTEGER");
      }
    });

    final List<String> tableStatements = _objectUnderTest.createTableStatements();
    assertEquals(2, tableStatements.size());

    final String createStatement = tableStatements.get(0);

    assertTrue(createStatement, createStatement.startsWith("CREATE TABLE schemaName.MY_TABLE"));
    assertTrue(createStatement, createStatement.contains("ID INTEGER NOT NULL"));
    assertTrue(createStatement, createStatement.contains("NAME VARCHAR(100) NOT NULL"));
  }

  @Test
  public void testCreateConstraintName() throws Exception {
    assertEquals("FK_NAME_1", _objectUnderTest.createConstraintName("FK_", "NAME_", 1));
    final String constraintName = _objectUnderTest.createConstraintName("FK_", "AUFTRAG_STELLUNGNAHME_HALTUNGSTELLUNGNAHME_ZU_HALTUNG_ID_PARENT_ID__ID_", 1);

    assertFalse("FK_AUFTRAG_STELLUNGNAHME_HALTUNGSTELLUNGNAHME_ZU_HALTUNG_ID_PARENT_ID__ID_1".equals(constraintName));
    assertEquals(42, constraintName.length());
    assertEquals(42, _objectUnderTest.getTargetMaxNameLength());
  }

  @Test
  public void testForeignKey() throws Exception {
    final ForeignKeyMetaData foreignKeyMetaData = _databaseMetaData.getTableMetaData().get(0).getImportedForeignKeys().get(0);
    final String sql = _objectUnderTest.createForeignKey(foreignKeyMetaData);

    assertEquals("ALTER TABLE schemaName.MY_TABLE1 ADD CONSTRAINT FK_Name FOREIGN KEY (NAME) REFERENCES schemaName.MY_TABLE2(NAME);", sql);
  }

  @Test
  public void testIndex() throws Exception {
    final ColumnMetaData columnMetaData = _databaseMetaData.getTableMetaData().get(0).getColumnMetaData("name");
    final IndexMetaData index = _databaseMetaData.getTableMetaData().get(0).getIndexesContainingColumn(columnMetaData).get(0);
    final String sql = _objectUnderTest.createIndex(index);

    assertEquals("CREATE UNIQUE INDEX Name_IDX1 ON schemaName.MY_TABLE1(NAME);", sql);
  }
}
