package de.akquinet.jbosscc.guttenbase.defaults.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * By default return column with same name ignoring case.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultColumnMapper implements ColumnMapper
{
  @Override
  public ColumnMapperResult map(final ColumnMetaData source, final TableMetaData targetTableMetaData) throws SQLException
  {
    final String columnName = source.getColumnName();
    final ColumnMetaData columnMetaData = targetTableMetaData.getColumnMetaData(columnName);
    final List<ColumnMetaData> result = columnMetaData != null ? new ArrayList<>(Collections.singletonList(columnMetaData))
        : new ArrayList<>();
    return new ColumnMapperResult(result);
  }
}
