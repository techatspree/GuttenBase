package de.akquinet.jbosscc.guttenbase.configuration;

import java.sql.Connection;
import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Configuration methods for target data base. Implementations may execute specific initialization code before and after operations are
 * executed.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface TargetDatabaseConfiguration extends DatabaseConfiguration {
  /**
   * If running within a container managed transaction such as within an EJB we cannot call commit() on the connection. This method should
   * return false then.
   */
  boolean mayCommit();

  /**
   * Called before any action is performed which may alter the state of the target data base.
   * 
   * Implementing classes usually disable foreign key and other constraints temporarily.
   */
  void initializeTargetConnection(Connection connection, String connectorId) throws SQLException;

  /**
   * Called after actions have been performed.
   * 
   * Implementing classes usually re-enable foreign key and other constraints.
   */
  void finalizeTargetConnection(Connection connection, String connectorId) throws SQLException;

  /**
   * Called before an INSERT clause is executed. E.g., in order to disable foreign key constraints. Note that an INSERT statement may have
   * multiple VALUES clauses.
   */
  void beforeInsert(final Connection connection, String connectorId, TableMetaData table) throws SQLException;

  /**
   * Called after an INSERT clause is executed. E.g., in order to re-enable foreign key constraints.
   */
  void afterInsert(final Connection connection, String connectorId, TableMetaData table) throws SQLException;

  /**
   * Called before a new row of data (VALUES clause) is added to the INSERT statement. Note that an INSERT statement may have multiple
   * VALUES clauses. This method will be called for every VALUES clause.
   */
  void beforeNewRow(final Connection connection, String connectorId, TableMetaData table) throws SQLException;

  /**
   * Called after a new row of data (VALUES clause) has been added to the INSERT statement.
   */
  void afterNewRow(final Connection connection, String connectorId, TableMetaData table) throws SQLException;
}
