package de.akquinet.jbosscc.guttenbase.mapping;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

import java.math.BigDecimal;

/**
 * Map Long/Bigint to BigDecimal
 *
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class BigIntLongToBigDecimalColumnDataMapper implements ColumnDataMapper {
  @Override
  public Object map(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData, final Object value)
      {
    return value == null ? null : new BigDecimal(value.toString());
  }

  @Override
  public boolean isApplicable(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData) {
    return true;
  }
}