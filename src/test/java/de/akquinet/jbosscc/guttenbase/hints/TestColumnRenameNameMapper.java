package de.akquinet.jbosscc.guttenbase.hints;


import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * look at columnName starts with some value and replace them
 * <p>
 * Created by Marynasuprun on 26.03.2017.
 */
public class TestColumnRenameNameMapper implements ColumnMapper {
  private final Map<String, String> replacementsColumns = new HashMap<>();

  @Override
  public ColumnMapperResult map(ColumnMetaData source, TableMetaData targetTableMetaData) {
    final String defaultColumnName = source.getColumnName();
    final String columnName = replacementsColumns.getOrDefault(defaultColumnName, defaultColumnName);
    final ColumnMetaData columnMetaData2 = targetTableMetaData.getColumnMetaData(columnName);

    return new ColumnMapperResult(Collections.singletonList(columnMetaData2));
  }

  @Override
  public String mapColumnName(ColumnMetaData source, TableMetaData targetTableMetaData) {
    final String result = source.getColumnName();
    final String columnName = replacementsColumns.get(result);

    if (columnName == null){
      return result;
    }
    else{
      return columnName;
    }
  }

  public TestColumnRenameNameMapper addReplacement(final String sourceComn, final String targetColumn) {
    replacementsColumns.put(sourceComn, targetColumn);
    return this;
  }
}


