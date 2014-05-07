package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultColumnDataMapperProvider;
import de.akquinet.jbosscc.guttenbase.hints.ColumnDataMapperProviderHint;
import de.akquinet.jbosscc.guttenbase.mapping.BigDecimalToLongColumnDataMapper;
import de.akquinet.jbosscc.guttenbase.mapping.LongToBigDecimalColumnDataMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnDataMapperProvider;
import de.akquinet.jbosscc.guttenbase.mapping.TimestampToDateColumnDataMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;

/**
 * Default implementation. You may inherit from this class and override {@link #addMappings(DefaultColumnDataMapperProvider)} to
 * add further mappings.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultColumnDataMapperProviderHint extends ColumnDataMapperProviderHint
{
  @Override
  public ColumnDataMapperProvider getValue()
  {
    final DefaultColumnDataMapperProvider result = new DefaultColumnDataMapperProvider();

    addMappings(result);

    return result;
  }

  /**
   * May be overridden to add further mappings
   */
  protected void addMappings(final DefaultColumnDataMapperProvider columnDataMapperFactory)
  {
    columnDataMapperFactory.addMapping(ColumnType.CLASS_TIMESTAMP, ColumnType.CLASS_DATE, new TimestampToDateColumnDataMapper());
    columnDataMapperFactory.addMapping(ColumnType.CLASS_LONG, ColumnType.CLASS_BIGDECIMAL,
        new LongToBigDecimalColumnDataMapper());
    columnDataMapperFactory
        .addMapping(ColumnType.CLASS_BIGDECIMAL, ColumnType.CLASS_LONG, new BigDecimalToLongColumnDataMapper());
    columnDataMapperFactory.addMapping(ColumnType.CLASS_INTEGER, ColumnType.CLASS_BIGDECIMAL,
        new LongToBigDecimalColumnDataMapper());
  }
}
