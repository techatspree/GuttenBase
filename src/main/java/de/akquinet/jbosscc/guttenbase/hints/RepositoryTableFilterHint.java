package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryTableFilter;

/**
 * This filter is applied when @see {@link ConnectorRepository#getDatabaseMetaData(String)} is called.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 * <p>
 * <p>
 * Hint is used by {@link ConnectorRepository#getDatabaseMetaData(String)} when returning table meta data
 *
 * @author M. Dahm
 */
public abstract class RepositoryTableFilterHint implements ConnectorHint<RepositoryTableFilter> {
  @Override
  public final Class<RepositoryTableFilter> getConnectorHintType() {
    return RepositoryTableFilter.class;
  }
}
