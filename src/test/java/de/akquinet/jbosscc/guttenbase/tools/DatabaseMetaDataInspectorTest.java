package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DatabaseMetaDataInspectorTest extends AbstractGuttenBaseTest {
	private static final String CONNECTOR_ID = "derby";

	@Before
	public void setup() throws Exception {
		_connectorRepository.addConnectionInfo(CONNECTOR_ID, new TestDerbyConnectionInfo());

		new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID, "/ddl/tables.sql");
	}

	@Test
	public void testMetaData() throws Exception {
		final DatabaseMetaData databaseMetaData = _connectorRepository.getDatabaseMetaData(CONNECTOR_ID);
		assertNotNull(databaseMetaData);
		assertEquals("Apache Derby", databaseMetaData.getDatabaseMetaData().getDatabaseProductName());
		assertEquals(128, databaseMetaData.getDatabaseMetaData().getMaxColumnNameLength());

		assertEquals(6, databaseMetaData.getTableMetaData().size());
		final TableMetaData userTableMetaData = databaseMetaData.getTableMetaData("FOO_USER");
		assertNotNull(userTableMetaData);

		final ColumnMetaData idColumn = checkIdColumnInformation(userTableMetaData);

		checkIndexInformation(userTableMetaData);

		checkForeignKeyInformation(idColumn);

		checkPrimaryKeyInformation(databaseMetaData);
	}

	private void checkPrimaryKeyInformation(final DatabaseMetaData databaseMetaData) {
		final TableMetaData userRolesTableMetaData = databaseMetaData.getTableMetaData("FOO_USER_ROLES");
		assertNotNull(userRolesTableMetaData);
		assertEquals(2, userRolesTableMetaData.getPrimaryKeyColumns().size());
	}

	private void checkForeignKeyInformation(final ColumnMetaData idColumn) {
		assertEquals(2, idColumn.getReferencedByColumn().size());
		final ColumnMetaData fkColumn = idColumn.getReferencedByColumn().get(0);
		assertEquals(idColumn, fkColumn.getReferencedColumn());
		assertEquals("USER_ID", fkColumn.getColumnName());
	}

	private ColumnMetaData checkIdColumnInformation(final TableMetaData userTableMetaData) {
		assertEquals(6, userTableMetaData.getColumnCount());
		final ColumnMetaData idColumn = userTableMetaData.getColumnMetaData("ID");
		assertNotNull(idColumn);
		assertEquals("BIGINT", idColumn.getColumnTypeName());
		assertTrue(idColumn.isPrimaryKey());
		assertFalse(idColumn.isNullable());
		assertEquals(singletonList(idColumn), userTableMetaData.getPrimaryKeyColumns());
		return idColumn;
	}

	private void checkIndexInformation(final TableMetaData userTableMetaData) {
		assertEquals(3, userTableMetaData.getIndexes().size());
		final IndexMetaData indexMetaData = userTableMetaData.getIndexMetaData("USERNAME_IDX");
		assertNotNull(indexMetaData);
		assertTrue(indexMetaData.isAscending());
		assertTrue(indexMetaData.isUnique());
		assertEquals(1, indexMetaData.getColumnMetaData().size());
		final ColumnMetaData indexedColumn = indexMetaData.getColumnMetaData().get(0);
		assertEquals("USERNAME", indexedColumn.getColumnName());

		final List<IndexMetaData> indexesForColumn = userTableMetaData.getIndexesContainingColumn(indexedColumn);
		assertEquals(singletonList(indexMetaData), indexesForColumn);
	}
}
