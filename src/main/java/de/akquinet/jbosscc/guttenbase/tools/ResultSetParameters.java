package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Set fetch size and result set parameters, i.e.
 * <pre>
 * stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
 * stmt.setFetchSize(100);
 * </pre>
 *
 *
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 * @see MaxNumberOfDataItems
 */

public interface ResultSetParameters {
  int getFetchSize(TableMetaData tableMetaData);

  int getResultSetType(TableMetaData tableMetaData);

  int getResultSetConcurrency(TableMetaData tableMetaData);
}
