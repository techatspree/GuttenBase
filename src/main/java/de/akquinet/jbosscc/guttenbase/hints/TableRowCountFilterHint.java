package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.repository.TableRowCountFilter;
import de.akquinet.jbosscc.guttenbase.repository.impl.DatabaseMetaDataInspectorTool;

/**
 * Some tables are really big and computing the row count may take too long for the data base.
 * <p></p>
 * Using this hint the @see {@link DatabaseMetaDataInspectorTool} will compute the row count only
 * for the given tables.
 */
public abstract class TableRowCountFilterHint implements ConnectorHint<TableRowCountFilter> {
  @Override
  public final Class<TableRowCountFilter> getConnectorHintType() {
    return TableRowCountFilter.class;
  }
}
