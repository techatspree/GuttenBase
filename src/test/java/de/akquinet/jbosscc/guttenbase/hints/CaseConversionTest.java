package de.akquinet.jbosscc.guttenbase.hints;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultTableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.builder.DatabaseMetaDataBuilder;
import de.akquinet.jbosscc.guttenbase.meta.builder.TableMetaDataBuilder;

public class CaseConversionTest
{
  @Test
  public void testDefaultNameMapper() throws Exception
  {
    final DatabaseMetaDataBuilder databaseMetaDataBuilder = new DatabaseMetaDataBuilder().setSchema("schema");
    final TableMetaDataBuilder tableMetaDataBuilder = new TableMetaDataBuilder(databaseMetaDataBuilder).setTableName("Table");

    assertEquals("schema.Table", new DefaultTableNameMapper().mapTableName(tableMetaDataBuilder.build()));
    assertEquals("schema.table", new DefaultTableNameMapper(CaseConversionMode.LOWER).mapTableName(tableMetaDataBuilder.build()));
    assertEquals("schema.TABLE", new DefaultTableNameMapper(CaseConversionMode.UPPER).mapTableName(tableMetaDataBuilder.build()));
  }
}
