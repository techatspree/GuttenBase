package de.akquinet.jbosscc.guttenbase.tools;

import java.io.File;

import org.junit.Before;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;
import de.akquinet.jbosscc.guttenbase.utils.Util;

public abstract class AbstractGuttenBaseTest {
	/**
	 * Place all DB data in temporary directory. Pure in-memory DBs are faster but mess up when running multiple tests.
	 */
	public static final File DB_DIRECTORY = new File("target/db");

	protected final ConnectorRepository _connectorRepository = new ConnectorRepositoryImpl();

	@Before
	public void dropTables() {
		Util.deleteDirectory(DB_DIRECTORY);
	}
}
