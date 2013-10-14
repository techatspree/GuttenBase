package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import de.akquinet.jbosscc.guttenbase.utils.ScriptExecutorToolProgressIndicator;

/**
 * Select implementation of progress indicator. May be simple logger or fancy UI.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @Applicable-For-Target
 * @Hint-Used-By {@link ScriptExecutorTool}
 * @author M. Dahm
 */
public abstract class ScriptExecutorProgressIndicatorHint implements ConnectorHint<ScriptExecutorToolProgressIndicator>
{
  @Override
  public Class<ScriptExecutorToolProgressIndicator> getConnectorHintType()
  {
    return ScriptExecutorToolProgressIndicator.class;
  }
}
