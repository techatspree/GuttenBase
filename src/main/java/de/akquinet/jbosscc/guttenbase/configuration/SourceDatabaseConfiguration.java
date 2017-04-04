package de.akquinet.jbosscc.guttenbase.configuration;

import java.sql.Connection;
import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Configuration methods for source data base. Implementations may execute specific initialization code before and after operations are
 * executed.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public interface SourceDatabaseConfiguration extends DatabaseConfiguration {
  /**
   * Called before any real action is performed.
   */
  void initializeSourceConnection(Connection connection, String connectorId) throws SQLException;

  /**
   * Called after actions have been performed.
   */
  void finalizeSourceConnection(Connection connection, String connectorId) throws SQLException;

  /**
   * Called before a SELECT clause is executed.
   */
  void beforeSelect(final Connection connection, String connectorId, TableMetaData table) throws SQLException;

  /**
   * Called after a SELECT clause is executed.
   */
  void afterSelect(final Connection connection, String connectorId, TableMetaData table) throws SQLException;
}
