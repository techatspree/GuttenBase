package de.akquinet.jbosscc.guttenbase.configuration;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;
import de.akquinet.jbosscc.guttenbase.tools.AbstractGuttenBaseTest;

public class TestHsqlConnectionInfo extends URLConnectorInfoImpl {
	private static final long serialVersionUID = 1L;
	private static int count = 1;

	public TestHsqlConnectionInfo() {
		super("jdbc:hsqldb:" + AbstractGuttenBaseTest.DB_DIRECTORY + "/hsqldb" + count++, "sa", "", "org.hsqldb.jdbcDriver", "",
				DatabaseType.HSQLDB);
	}
}
