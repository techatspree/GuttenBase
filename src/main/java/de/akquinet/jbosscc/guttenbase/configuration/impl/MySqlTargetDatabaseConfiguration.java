package de.akquinet.jbosscc.guttenbase.configuration.impl;

import java.sql.Connection;
import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Implementation for MYSQL data base.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
@SuppressWarnings("JavaDoc")
public class MySqlTargetDatabaseConfiguration extends DefaultTargetDatabaseConfiguration
{
  private final boolean _disableUniqueChecks;

  /**
   * @param connectorRepository
   * @param disableUniqueChecks
   * disable unique checks, too. Not just foreign key constraints.
   */
  public MySqlTargetDatabaseConfiguration(final ConnectorRepository connectorRepository, final boolean disableUniqueChecks)
  {
    super(connectorRepository);

    _disableUniqueChecks = disableUniqueChecks;
  }

  public MySqlTargetDatabaseConfiguration(final ConnectorRepository connectorRepository)
  {
    this(connectorRepository, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeTargetConnection(final Connection connection, final String connectorId) throws SQLException
  {
    if (connection.getAutoCommit())
    {
      connection.setAutoCommit(false);
    }

    setReferentialIntegrity(connection, false);

    if (_disableUniqueChecks)
    {
      setUniqueChecks(connection, false);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void finalizeTargetConnection(final Connection connection, final String connectorId) throws SQLException
  {
    setReferentialIntegrity(connection, true);

    if (_disableUniqueChecks)
    {
      setUniqueChecks(connection, true);
    }
  }

  private void setReferentialIntegrity(final Connection connection, final boolean enable) throws SQLException
  {
    executeSQL(connection, "SET FOREIGN_KEY_CHECKS = " + (enable ? "1" : "0") + ";");
  }

  private void setUniqueChecks(final Connection connection, final boolean enable) throws SQLException
  {
    executeSQL(connection, "SET UNIQUE_CHECKS = " + (enable ? "1" : "0") + ";");
  }

  //  private void setCaseSensitivityMode(final Connection connection) throws SQLException
  //  {
  //    executeSQL(connection,
  //        "SET @OLD_LOWER_CASE_TABLE_NAMES=@@LOWER_CASE_TABLE_NAMES, LOWER_CASE_TABLE_NAMES = " + _caseSensitivityMode + ";");
  //  }
  //
  //  private void resetCaseSensitivityMode(final Connection connection) throws SQLException
  //  {
  //    executeSQL(connection, "SET LOWER_CASE_TABLE_NAMES=@OLD_LOWER_CASE_TABLE_NAMES;");
  //  }
}
