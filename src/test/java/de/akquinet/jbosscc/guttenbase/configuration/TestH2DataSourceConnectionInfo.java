package de.akquinet.jbosscc.guttenbase.configuration;

import org.h2.jdbcx.JdbcDataSource;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.DataSourceConnectorInfo;
import de.akquinet.jbosscc.guttenbase.tools.AbstractGuttenBaseTest;

public class TestH2DataSourceConnectionInfo extends DataSourceConnectorInfo {
	private static final long serialVersionUID = 1L;

	public TestH2DataSourceConnectionInfo() {
		super(createDataSource(), null, null, "", DatabaseType.H2DB);
	}

	private static JdbcDataSource createDataSource() {
		final JdbcDataSource jdbcDataSource = new JdbcDataSource();
		jdbcDataSource.setURL("jdbc:h2:" + AbstractGuttenBaseTest.DB_DIRECTORY + "/h2ds");
		jdbcDataSource.setUser("sa");
		jdbcDataSource.setPassword("sa");
		return jdbcDataSource;
	}
}
