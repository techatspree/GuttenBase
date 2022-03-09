package de.akquinet.jbosscc.guttenbase.configuration;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

import java.sql.Connection;

/**
 * Implementations may execute specific initialization code before and after operations are executed.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public interface DatabaseConfiguration {
  /**
   * Called before table is copied
   */
  void beforeTableCopy(final Connection connection, String connectorId, TableMetaData table);

  /**
   * Called after table has been copied
   */
  void afterTableCopy(final Connection connection, String connectorId, TableMetaData table);
}
