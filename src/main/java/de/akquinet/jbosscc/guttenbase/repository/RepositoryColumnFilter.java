package de.akquinet.jbosscc.guttenbase.repository;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

import java.sql.SQLException;

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
public interface RepositoryColumnFilter {
  boolean accept(ColumnMetaData column) throws SQLException;
}
