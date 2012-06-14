package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.TestH2ConnectionInfo;

public class H2SchemaCreatorTest extends AbstractSchemaCreatorTest {
  public H2SchemaCreatorTest() {
    super("h2db", new TestH2ConnectionInfo());
  }
}
