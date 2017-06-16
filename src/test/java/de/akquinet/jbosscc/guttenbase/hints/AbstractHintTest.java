package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;
import de.akquinet.jbosscc.guttenbase.configuration.TestH2ConnectionInfo;
import de.akquinet.jbosscc.guttenbase.tools.AbstractGuttenBaseTest;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

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
  private final String _sourceTableSchemaScript;
  private final String _targetTableSchemaScript;
  private final String _dataScript;

  public AbstractHintTest(final String sourceTableSchemaScript, final String targetTableSchemaScript, final String dataScript) {
    _sourceTableSchemaScript = sourceTableSchemaScript;
    _targetTableSchemaScript = targetTableSchemaScript;
    _dataScript = dataScript;
  }

  @Before
  public final void setupTables() throws Exception {
    _connectorRepository.addConnectionInfo(SOURCE, new TestDerbyConnectionInfo());
    _connectorRepository.addConnectionInfo(TARGET, new TestH2ConnectionInfo());
    final ScriptExecutorTool scriptExecutorTool = new ScriptExecutorTool(_connectorRepository, "UTF-8");

    scriptExecutorTool.executeFileScript(SOURCE, _sourceTableSchemaScript);
    scriptExecutorTool.executeFileScript(TARGET, _targetTableSchemaScript);
    scriptExecutorTool.executeFileScript(SOURCE, false, false, _dataScript);
  }

  @Test
  public void testTableCopy() throws Exception {
    assertFalse(new SchemaComparatorTool(_connectorRepository).check(SOURCE, TARGET).isSevere());

    new DefaultTableCopyTool(_connectorRepository).copyTables(SOURCE, TARGET);
    new CheckEqualTableDataTool(_connectorRepository).checkTableData(SOURCE, TARGET);

    executeChecks();
  }

  protected void executeChecks() throws Exception {
  }
}
