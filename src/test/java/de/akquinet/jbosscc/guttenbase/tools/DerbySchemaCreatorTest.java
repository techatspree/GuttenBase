package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;

public class DerbySchemaCreatorTest extends AbstractSchemaCreatorTest {
  public DerbySchemaCreatorTest() {
    super("derby", new TestDerbyConnectionInfo());
  }
}
