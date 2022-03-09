package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryColumnFilter;

/**
 * This filter is applied when @see {@link ConnectorRepository#getDatabaseMetaData(String)} is called.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 * <p>
 * <p>
 * Hint is used by {@link ConnectorRepository#getDatabaseMetaData(String)} when returning table meta data and their respective columns
 *
 * @author M. Dahm
 */
public abstract class RepositoryColumnFilterHint implements ConnectorHint<RepositoryColumnFilter> {
  @Override
  public final Class<RepositoryColumnFilter> getConnectorHintType() {
    return RepositoryColumnFilter.class;
  }
}
