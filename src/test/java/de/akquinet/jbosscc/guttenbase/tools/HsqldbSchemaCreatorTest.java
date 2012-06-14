package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;

public class HsqldbSchemaCreatorTest extends AbstractSchemaCreatorTest {
  public HsqldbSchemaCreatorTest() {
    super("hsqldb", new TestHsqlConnectionInfo());
  }
}
