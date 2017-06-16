package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.SelectWhereClauseHint;
import de.akquinet.jbosscc.guttenbase.tools.SelectWhereClause;

/**
 * Default is no WHERE clause.
 * <p/>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DefaultSelectWhereClauseHint extends SelectWhereClauseHint {
  @Override
  public SelectWhereClause getValue() {
    return tableMetaData -> "";
  }
}
