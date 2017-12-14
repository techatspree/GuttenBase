package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.tools.SelectWhereClause;

/**
 * Optionally configure the SELECT statement created to read data from source tables with a WHERE clause.
 * <p></p>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 *
 * Hint is used by {@link de.akquinet.jbosscc.guttenbase.tools.AbstractTableCopyTool}
 * Hint is used by {@link de.akquinet.jbosscc.guttenbase.repository.impl.DatabaseMetaDataInspectorTool}
 */
public abstract class SelectWhereClauseHint implements ConnectorHint<SelectWhereClause> {
  @Override
  public final Class<SelectWhereClause> getConnectorHintType() {
    return SelectWhereClause.class;
  }
}
