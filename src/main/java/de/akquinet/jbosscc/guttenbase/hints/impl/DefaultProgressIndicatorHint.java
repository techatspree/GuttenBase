package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.ProgressIndicatorHint;
import de.akquinet.jbosscc.guttenbase.utils.LoggingProgressIndicator;
import de.akquinet.jbosscc.guttenbase.utils.TableCopyProgressIndicator;

/**
 * By default return logging implementation.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultProgressIndicatorHint extends ProgressIndicatorHint
{
  @Override
  public TableCopyProgressIndicator getValue()
  {
    return new LoggingProgressIndicator();
  }
}
