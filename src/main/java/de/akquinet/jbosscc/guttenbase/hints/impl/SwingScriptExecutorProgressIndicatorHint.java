package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.ScriptExecutorProgressIndicatorHint;
import de.akquinet.jbosscc.guttenbase.utils.ScriptExecutorProgressIndicator;
import de.akquinet.jbosscc.guttenbase.utils.SwingScriptExecutorProgressIndicator;

/**
 * Use UI to show progress.
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public final class SwingScriptExecutorProgressIndicatorHint extends ScriptExecutorProgressIndicatorHint
{
  @Override
  public ScriptExecutorProgressIndicator getValue()
  {
    return new SwingScriptExecutorProgressIndicator();
  }
}
