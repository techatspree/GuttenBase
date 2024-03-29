package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.TestH2ConnectionInfo;
import de.akquinet.jbosscc.guttenbase.sql.SQLLexer;
import de.akquinet.jbosscc.guttenbase.tools.schema.CopySchemaTool;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class CopySchemaToolTest extends AbstractGuttenBaseTest {
  private static final String CONNECTOR_ID = "hsqldb";
  private static final String TARGET = "jens";

  private final CopySchemaTool _objectUnderTest = new CopySchemaTool(_connectorRepository);

  @Before
  public void setup() throws Exception {
    _connectorRepository.addConnectionInfo(CONNECTOR_ID, new TestH2ConnectionInfo());
    _connectorRepository.addConnectionInfo(TARGET, new TestH2ConnectionInfo("BLA"));
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID, "/ddl/tables.sql");
  }

  @Test
  public void testScript() throws Exception {
    final List<String> script = _objectUnderTest.createDDLScript(CONNECTOR_ID, TARGET);
    final List<String> parsedScript = new SQLLexer(script).parse();

    assertTrue(parsedScript
        .contains("CREATE TABLE BLA.FOO_COMPANY ( ID BIGINT NOT NULL, SUPPLIER CHAR(1), NAME VARCHAR(100) NOT NULL )"));
    assertTrue(parsedScript.contains("ALTER TABLE BLA.FOO_COMPANY ADD CONSTRAINT PK_FOO_COMPANY PRIMARY KEY (ID)"));
    assertTrue(parsedScript
        .contains("ALTER TABLE BLA.FOO_USER_ROLES ADD CONSTRAINT FKFOO_USER_ROLES FOREIGN KEY (ROLE_ID) REFERENCES BLA.FOO_ROLE(ID)"));
    assertTrue(parsedScript
        .contains("ALTER TABLE BLA.FOO_USER_ROLES ADD CONSTRAINT FKFOO_USER_ROLES2 FOREIGN KEY (USER_ID) REFERENCES BLA.FOO_USER(ID)"));
  }
}
