package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultTableRowCountFilter;
import de.akquinet.jbosscc.guttenbase.hints.TableRowCountFilterHint;
import de.akquinet.jbosscc.guttenbase.repository.TableRowCountFilter;

/**
 * By default compute row count for all tables
 */
public class DefaultTableRowCountFilterHint extends TableRowCountFilterHint {
  @Override
  public TableRowCountFilter getValue() {
    return new DefaultTableRowCountFilter();
  }
}
