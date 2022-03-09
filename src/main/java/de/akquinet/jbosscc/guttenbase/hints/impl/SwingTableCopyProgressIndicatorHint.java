package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.TableCopyProgressIndicatorHint;
import de.akquinet.jbosscc.guttenbase.utils.SwingTableCopyProgressIndicator;
import de.akquinet.jbosscc.guttenbase.utils.TableCopyProgressIndicator;

/**
 * Use UI to show progress.
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public final class SwingTableCopyProgressIndicatorHint extends TableCopyProgressIndicatorHint {
  @Override
  public TableCopyProgressIndicator getValue() {
    return new SwingTableCopyProgressIndicator();
  }
}
