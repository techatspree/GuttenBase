package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.ProgressIndicatorHint;
import de.akquinet.jbosscc.guttenbase.utils.ProgressIndicator;
import de.akquinet.jbosscc.guttenbase.utils.SwingProgressIndicator;

/**
 * Use UI to show progress.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public final class SwingProgressIndicatorHint extends ProgressIndicatorHint
{
  @Override
  public ProgressIndicator getValue()
  {
    return new SwingProgressIndicator();
  }
}
