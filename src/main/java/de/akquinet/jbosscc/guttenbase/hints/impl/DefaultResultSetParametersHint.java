package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.ResultSetParametersHint;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.tools.ResultSetParameters;

import java.sql.ResultSet;

/**
 * Default result set fetch size is 2000. Result set type is ResultSet.TYPE_FORWARD_ONLY,
 * and concurrency type is ResultSet.CONCUR_READ_ONLY.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DefaultResultSetParametersHint extends ResultSetParametersHint {

  @Override
  public ResultSetParameters getValue() {
    return new DefaultResultSetParameters();
  }

  public static class DefaultResultSetParameters implements ResultSetParameters {
    @Override
    public int getFetchSize(TableMetaData tableMetaData) {
      return 2000;
    }

    @Override
    public int getResultSetType(TableMetaData tableMetaData) {
      return ResultSet.TYPE_FORWARD_ONLY;
    }

    @Override
    public int getResultSetConcurrency(TableMetaData tableMetaData) {
      return ResultSet.CONCUR_READ_ONLY;
    }
  }
}
