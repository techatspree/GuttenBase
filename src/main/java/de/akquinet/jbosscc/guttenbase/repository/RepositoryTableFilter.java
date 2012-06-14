package de.akquinet.jbosscc.guttenbase.repository;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * This filter is applied when @see {@link ConnectorRepository#getDatabaseMetaData(String)} is called.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface RepositoryTableFilter {
  boolean accept(TableMetaData table) throws SQLException;
}
