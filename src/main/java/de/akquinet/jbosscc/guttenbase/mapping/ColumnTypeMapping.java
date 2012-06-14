package de.akquinet.jbosscc.guttenbase.mapping;

import de.akquinet.jbosscc.guttenbase.meta.ColumnType;

/**
 * Container for column type mapping information.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ColumnTypeMapping {
  private final ColumnType _sourceColumnType;
  private final ColumnType _targetColumnType;
  private final ColumnDataMapper _columnDataMapper;

  public ColumnTypeMapping(final ColumnType sourceColumnType, final ColumnType targetColumnType, final ColumnDataMapper columnDataMapper) {
    _sourceColumnType = sourceColumnType;
    _targetColumnType = targetColumnType;
    _columnDataMapper = columnDataMapper;
  }

  public ColumnType getSourceColumnType() {
    return _sourceColumnType;
  }

  public ColumnType getTargetColumnType() {
    return _targetColumnType;
  }

  public ColumnDataMapper getColumnDataMapper() {
    return _columnDataMapper;
  }
}