package de.akquinet.jbosscc.guttenbase.mapping;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Map Timestamp to Date as some databases use a DATETIME column others a simple DATE.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public final class TimestampToDateColumnDataMapper implements ColumnDataMapper {
  @Override
  public Object map(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData, final Object value) {
    if (value == null) {
      return null;
    } else {
      return new Date(((Timestamp) value).getTime());
    }
  }

  @Override
  public boolean isApplicable(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData) {
    return true;
  }
}