package de.akquinet.jbosscc.guttenbase.configuration;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;
import de.akquinet.jbosscc.guttenbase.tools.AbstractGuttenBaseTest;

public class TestH2ConnectionInfo extends URLConnectorInfoImpl {
	private static final long serialVersionUID = 1L;
	private static int count = 1;

  public TestH2ConnectionInfo(final String schema) {
    super("jdbc:h2:" + AbstractGuttenBaseTest.DB_DIRECTORY + "/h2" + count++, "sa", "sa", "org.h2.Driver", schema, DatabaseType
      .H2DB);
  }

  public TestH2ConnectionInfo() {
    this("");
  }
}
