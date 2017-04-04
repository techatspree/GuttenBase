package de.akquinet.jbosscc.guttenbase.defaults.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Utility mapper fro dropping columns of tables.
 * <p>
 * &copy; 2013 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DroppingColumnMapper extends DefaultColumnMapper
{
  private final Map<String, List<String>> _droppedColumns = new HashMap<>();

  public DroppingColumnMapper addDroppedColumn(final String targetTableName, final String sourceColumName)
  {
    assert targetTableName != null : "tableName != null";
    assert sourceColumName != null : "columName != null";

    List<String> dropped = _droppedColumns.computeIfAbsent(targetTableName.toUpperCase(), k -> new ArrayList<>());

    dropped.add(sourceColumName.toUpperCase());
    return this;
  }

  @Override
  public ColumnMapperResult map(final ColumnMetaData source, final TableMetaData targetTableMetaData) throws SQLException
  {
    final List<String> columns = _droppedColumns.get(targetTableMetaData.getTableName().toUpperCase());

    if (columns != null && columns.contains(source.getColumnName().toUpperCase()))
    {
      return new ColumnMapperResult(new ArrayList<>(), true);
    }
    else
    {
      return super.map(source, targetTableMetaData);
    }
  }
}
