package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Optionally configure the SELECT statement created to read data from source tables with a WHERE clause.
 * <p/>
 * <p>&copy; 2012-2020 akquinet tech@spree</p>
 *
 * @author M. Dahm
 */
public interface SelectWhereClause {
  String getWhereClause(TableMetaData tableMetaData);
}
