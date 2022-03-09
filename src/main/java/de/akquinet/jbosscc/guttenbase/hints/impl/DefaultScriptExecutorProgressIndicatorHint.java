package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.ScriptExecutorProgressIndicatorHint;
import de.akquinet.jbosscc.guttenbase.utils.LoggingScriptExecutorProgressIndicator;
import de.akquinet.jbosscc.guttenbase.utils.ScriptExecutorProgressIndicator;

/**
 * By default return logging implementation.
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DefaultScriptExecutorProgressIndicatorHint extends ScriptExecutorProgressIndicatorHint {
  @Override
  public ScriptExecutorProgressIndicator getValue() {
    return new LoggingScriptExecutorProgressIndicator();
  }
}
