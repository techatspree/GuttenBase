package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import de.akquinet.jbosscc.guttenbase.utils.ScriptExecutorProgressIndicator;

/**
 * Select implementation of progress indicator. May be simple logger or fancy UI.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 *
 * Hint is used by {@link ScriptExecutorTool}
 * @author M. Dahm
 */
public abstract class ScriptExecutorProgressIndicatorHint implements ConnectorHint<ScriptExecutorProgressIndicator>
{
  @Override
  public Class<ScriptExecutorProgressIndicator> getConnectorHintType()
  {
    return ScriptExecutorProgressIndicator.class;
  }
}
