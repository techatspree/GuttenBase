package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultTableMapper;
import de.akquinet.jbosscc.guttenbase.meta.InternalDatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalTableMetaData;
import de.akquinet.jbosscc.guttenbase.meta.builder.DatabaseMetaDataBuilder;
import de.akquinet.jbosscc.guttenbase.meta.builder.TableMetaDataBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultTableMapperTest
{
  @Test
  public void testDefaultNameMapper() throws Exception
  {
    final DatabaseMetaDataBuilder databaseMetaDataBuilder = new DatabaseMetaDataBuilder().setSchema("schema");
    final TableMetaDataBuilder tableMetaDataBuilder = new TableMetaDataBuilder(databaseMetaDataBuilder)
      .setTableName("Table");
    final InternalDatabaseMetaData databaseMetaData = databaseMetaDataBuilder.build();
    final InternalTableMetaData tableMetaData = tableMetaDataBuilder.build();

    assertEquals("Table", new DefaultTableMapper().mapTableName(tableMetaData, databaseMetaData));
    assertEquals("schema.Table", new DefaultTableMapper().fullyQualifiedTableName(tableMetaData, databaseMetaData));
    assertEquals("schema.table", new DefaultTableMapper(CaseConversionMode.LOWER).fullyQualifiedTableName(tableMetaData, databaseMetaData));
    assertEquals("schema.TABLE", new DefaultTableMapper(CaseConversionMode.UPPER).fullyQualifiedTableName(tableMetaData, databaseMetaData));
  }
}
