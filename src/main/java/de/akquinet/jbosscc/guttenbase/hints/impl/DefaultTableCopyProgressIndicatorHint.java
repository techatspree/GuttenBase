package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.TableCopyProgressIndicatorHint;
import de.akquinet.jbosscc.guttenbase.utils.LoggingTableCopyProgressIndicator;
import de.akquinet.jbosscc.guttenbase.utils.TableCopyProgressIndicator;

/**
 * By default return logging implementation.
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DefaultTableCopyProgressIndicatorHint extends TableCopyProgressIndicatorHint
{
  @Override
  public TableCopyProgressIndicator getValue()
  {
    return new LoggingTableCopyProgressIndicator();
  }
}
