package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.configuration.impl.DefaultSourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import org.junit.Before;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * Check that "life-cycle" methods are called correctly
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ConfigurationLifeCycleTest extends AbstractHintTest {
  private int _afterTableCopy;
  private int _beforeTableCopy;
  private int _afterSelect;
  private int _beforeSelect;
  private int _finalizeSourceConnection;
  private int _initializeSourceConnection;

  public ConfigurationLifeCycleTest() {
    super("/ddl/tables.sql", "/ddl/tables.sql", "/data/test-data.sql");
  }

  @Before
  public void setup() throws Exception {
    _connectorRepository.addSourceDatabaseConfiguration(DatabaseType.DERBY, new DefaultSourceDatabaseConfiguration(_connectorRepository) {

      @Override
      public void initializeSourceConnection(final Connection connection, final String connectorId) throws SQLException {
        super.initializeSourceConnection(connection, connectorId);
        _initializeSourceConnection++;
      }

      @Override
      public void finalizeSourceConnection(final Connection connection, final String connectorId) throws SQLException {
        _finalizeSourceConnection++;
      }

      @Override
      public void beforeSelect(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException {
        _beforeSelect++;
      }

      @Override
      public void afterSelect(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException {
        _afterSelect++;
      }

      @Override
      public void beforeTableCopy(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException {
        _beforeTableCopy++;
      }

      @Override
      public void afterTableCopy(final Connection connection, final String connectorId, final TableMetaData table) throws SQLException {
        _afterTableCopy++;
      }
    });
  }

  @Override
  protected void executeChecks() throws Exception {
    final int toolsCount = 3;
    final int tablesCount = 6;

    assertEquals(toolsCount, _initializeSourceConnection);
    assertEquals(toolsCount, _finalizeSourceConnection);
    assertEquals(tablesCount, _beforeTableCopy);
    assertEquals(tablesCount, _afterTableCopy);
    assertEquals(2 * tablesCount, _beforeSelect);
    assertEquals(2 * tablesCount, _afterSelect);
  }
}
