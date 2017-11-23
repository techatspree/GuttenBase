package de.akquinet.jbosscc.guttenbase.defaults.impl;

import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultColumnDataMapperProviderHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnDataMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnDataMapperProvider;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation. To add further mapping you should simply extend {@link DefaultColumnDataMapperProviderHint} and call
 * {@link #addMapping(ColumnType, ColumnType, ColumnDataMapper)} in the overridden
 * {@link DefaultColumnDataMapperProviderHint#addMappings(DefaultColumnDataMapperProvider)} method.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DefaultColumnDataMapperProvider implements ColumnDataMapperProvider
{
  private final Map<String, List<ColumnDataMapper>> _mappings = new HashMap<>();

  /**
   * {@inheritDoc}
   */
  @Override
  public ColumnDataMapper findMapping(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData,
      final ColumnType sourceColumnType, final ColumnType targetColumnType) throws SQLException
  {
    final List<ColumnDataMapper> columnDataMappers = findMapping(sourceColumnType, targetColumnType);

    for (final ColumnDataMapper columnDataMapper : columnDataMappers)
    {
      if (columnDataMapper.isApplicable(sourceColumnMetaData, targetColumnMetaData))
      {
        return columnDataMapper;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addMapping(final ColumnType sourceColumnType, final ColumnType targetColumnType,
      final ColumnDataMapper columnDataMapper)
  {
    assert columnDataMapper != null : "columnDataMapper != null";

    findMapping(sourceColumnType, targetColumnType).add(columnDataMapper);
  }

  private String createKey(final ColumnType sourceColumnType, final ColumnType targetColumnType)
  {
    assert sourceColumnType != null : "sourceColumnType != null";
    assert targetColumnType != null : "targetColumnType != null";

    return sourceColumnType.name() + ":" + targetColumnType.name();
  }

  private List<ColumnDataMapper> findMapping(final ColumnType sourceColumnType, final ColumnType targetColumnType)
  {
    final String key = createKey(sourceColumnType, targetColumnType);

    return _mappings.computeIfAbsent(key, k -> new ArrayList<>());
  }
}
