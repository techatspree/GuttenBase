package de.akquinet.jbosscc.guttenbase.mapping;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

import java.math.BigDecimal;

/**
 * Map BigDecimal to Long/Bigint
 * <p>
 * &copy; 2013 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class BigDecimalToLongColumnDataMapper implements ColumnDataMapper {
  @Override
  public boolean isApplicable(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData)
      {
    return true;
  }

  @Override
  public Object map(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData, final Object value)
      {
    if (value != null) {
      final BigDecimal bigDecimal = (BigDecimal) value;

      return bigDecimal.longValue();
    } else {
      return null;
    }
  }
}
