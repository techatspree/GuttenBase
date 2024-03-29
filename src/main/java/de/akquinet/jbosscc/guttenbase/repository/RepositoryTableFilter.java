package de.akquinet.jbosscc.guttenbase.repository;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * This filter is applied when @see {@link ConnectorRepository#getDatabaseMetaData(String)} is called.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public interface RepositoryTableFilter {
  boolean accept(TableMetaData table);
}
