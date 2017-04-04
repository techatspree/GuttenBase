package de.akquinet.jbosscc.guttenbase.hints;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryTableFilter;
import de.akquinet.jbosscc.guttenbase.tools.AbstractGuttenBaseTest;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;

/**
 * Filters tables when inquiring connector repository.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class RepositoryTableFilterHintTest extends AbstractGuttenBaseTest {
	public static final String SOURCE = "SOURCE";

	@Before
	public final void setupTables() throws Exception {
		_connectorRepository.addConnectionInfo(SOURCE, new TestHsqlConnectionInfo());
		new ScriptExecutorTool(_connectorRepository).executeFileScript(SOURCE, "/ddl/tables.sql");

	}

	@Test
	public void testFilter() throws Exception {
		assertEquals("Before", 6, _connectorRepository.getDatabaseMetaData(SOURCE).getTableMetaData().size());

		_connectorRepository.addConnectorHint(SOURCE, new RepositoryTableFilterHint() {
			@Override
			public RepositoryTableFilter getValue() {
				return table -> table.getTableName().toUpperCase().contains("USER");
			}
		});

		assertEquals("After", 3, _connectorRepository.getDatabaseMetaData(SOURCE).getTableMetaData().size());
	}
}
