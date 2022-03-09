package de.akquinet.jbosscc.guttenbase.configuration;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

public class TestDerbyConnectionInfo extends URLConnectorInfoImpl {
  private static final long serialVersionUID = 1L;
  private static int count = 1;

  public TestDerbyConnectionInfo() {
    super("jdbc:derby:target/db/derby" + count++ + ";create=true", "sa", "sa", "org.apache.derby.jdbc.EmbeddedDriver", "",
        DatabaseType.DERBY);
  }
}
