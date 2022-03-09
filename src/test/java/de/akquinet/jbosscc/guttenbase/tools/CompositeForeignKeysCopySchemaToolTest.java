package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.TestH2ConnectionInfo;
import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.sql.SQLLexer;
import de.akquinet.jbosscc.guttenbase.tools.schema.CopySchemaTool;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class CompositeForeignKeysCopySchemaToolTest extends AbstractGuttenBaseTest {
  private static final String CONNECTOR_ID = "hsqldb";
  private static final String TARGET = "h2";

  private final CopySchemaTool _objectUnderTest = new CopySchemaTool(_connectorRepository);

  @Before
  public void setup() throws Exception {
    _connectorRepository.addConnectionInfo(CONNECTOR_ID, new TestHsqlConnectionInfo());
    _connectorRepository.addConnectionInfo(TARGET, new TestH2ConnectionInfo("BLA"));
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID, "/ddl/composite_foreignkey_tables.sql");
  }

  @Test
  public void testScript() throws Exception {
    final List<String> script = _objectUnderTest.createDDLScript(CONNECTOR_ID, TARGET);
    final List<String> parsedScript = new SQLLexer(script).parse();

    assertTrue(parsedScript
        .contains("CREATE TABLE BLA.FOO_COMPANY ( ID BIGINT NOT NULL,  SUPPLIER CHARACTER(1),  NAME VARCHAR(100) NOT NULL )"));
    assertTrue(parsedScript.contains("ALTER TABLE BLA.FOO_COMPANY ADD CONSTRAINT PK_FOO_COMPANY PRIMARY KEY (ID)"));
    assertTrue(parsedScript
        .contains("ALTER TABLE BLA.FOO_USER_SYSTEMS ADD CONSTRAINT FK_FOO_USER_SYSTEMS FOREIGN KEY (USER_ID, USER_NAME) REFERENCES BLA.FOO_USER(ID, USER_NAME)"));
  }
}
