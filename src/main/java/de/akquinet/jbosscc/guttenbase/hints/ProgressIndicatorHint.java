package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.tools.AbstractTableCopyTool;
import de.akquinet.jbosscc.guttenbase.utils.ProgressIndicator;

/**
 * Select implementation of progress indicator. May be simple logger or fancy UI.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @Applicable-For-Target
 * @Hint-Used-By {@link AbstractTableCopyTool} to map columns
 * @author M. Dahm
 */
public abstract class ProgressIndicatorHint implements ConnectorHint<ProgressIndicator>
{
  @Override
  public Class<ProgressIndicator> getConnectorHintType()
  {
    return ProgressIndicator.class;
  }
}
