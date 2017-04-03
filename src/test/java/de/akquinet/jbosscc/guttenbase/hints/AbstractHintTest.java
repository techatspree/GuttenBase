package de.akquinet.jbosscc.guttenbase.hints;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;
import de.akquinet.jbosscc.guttenbase.configuration.TestH2ConnectionInfo;
import de.akquinet.jbosscc.guttenbase.tools.AbstractGuttenBaseTest;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;
import de.akquinet.jbosscc.guttenbase.tools.CheckSchemaCompatibilityTool;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;

/**
 * Super class for Hint tests
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
@SuppressWarnings("deprecation")
public abstract class AbstractHintTest extends AbstractGuttenBaseTest {
  public static final String SOURCE = "SOURCE";
  public static final String TARGET = "TARGET";
  private final String _tablesResource1;
  private final String _tablesResource2;
  private final String _dataResource1;

  public AbstractHintTest(final String tablesResource1, final String tablesResource2, final String dataResource1) {
    _tablesResource1 = tablesResource1;
    _tablesResource2 = tablesResource2;
    _dataResource1 = dataResource1;
  }

  @Before
  public final void setupTables() throws Exception {
    _connectorRepository.addConnectionInfo(SOURCE, new TestDerbyConnectionInfo());
    _connectorRepository.addConnectionInfo(TARGET, new TestH2ConnectionInfo());
    final ScriptExecutorTool scriptExecutorTool = new ScriptExecutorTool(_connectorRepository, "UTF-8");

    scriptExecutorTool.executeFileScript(SOURCE, _tablesResource1);
    scriptExecutorTool.executeFileScript(TARGET, _tablesResource2);
    scriptExecutorTool.executeFileScript(SOURCE, false, false, _dataResource1);
  }

  @Test
  public void testTableCopy() throws Exception {
    new CheckSchemaCompatibilityTool(_connectorRepository).checkTableConfiguration(SOURCE, TARGET);
    new DefaultTableCopyTool(_connectorRepository).copyTables(SOURCE, TARGET);
    new CheckEqualTableDataTool(_connectorRepository).checkTableData(SOURCE, TARGET);
    executeChecks();
  }

  protected void executeChecks() throws Exception {
  }
}
