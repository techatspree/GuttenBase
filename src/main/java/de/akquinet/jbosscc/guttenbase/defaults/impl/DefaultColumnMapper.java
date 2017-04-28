package de.akquinet.jbosscc.guttenbase.defaults.impl;

import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * By default return column with same name ignoring case.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DefaultColumnMapper implements ColumnMapper {
  private final CaseConversionMode _caseConversionMode;

  public DefaultColumnMapper(final CaseConversionMode caseConversionMode) {
    assert caseConversionMode != null : "caseConversionMode != null";
    _caseConversionMode = caseConversionMode;
  }

  public DefaultColumnMapper() {
    this(CaseConversionMode.NONE);
  }

  @Override
  public String mapColumnName(final ColumnMetaData columnMetaData, final TableMetaData targetTableMetaData) throws SQLException {
    return _caseConversionMode.convert(columnMetaData.getColumnName());
  }

  @Override
  public ColumnMapperResult map(final ColumnMetaData source, final TableMetaData targetTableMetaData) throws SQLException {
    final String columnName = source.getColumnName();
    final ColumnMetaData columnMetaData = targetTableMetaData.getColumnMetaData(columnName);
    final List<ColumnMetaData> result = columnMetaData != null ? new ArrayList<>(Collections.singletonList(columnMetaData))
      : new ArrayList<>();
    return new ColumnMapperResult(result);
  }
}
