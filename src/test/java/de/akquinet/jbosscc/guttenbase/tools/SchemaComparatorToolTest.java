package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.configuration.TestDerbyConnectionInfo;
import de.akquinet.jbosscc.guttenbase.configuration.TestHsqlConnectionInfo;
import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.MissingForeignKeyIssue;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.MissingIndexIssue;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssue;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssueType;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssues;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SchemaComparatorToolTest extends AbstractGuttenBaseTest {
  private static final String CONNECTOR_ID1 = "hsqldb";
  private static final String CONNECTOR_ID2 = "derby";

  private final SchemaComparatorTool _objectUnderTest = new SchemaComparatorTool(_connectorRepository);

  @Before
  public void setup() {
    _connectorRepository.addConnectionInfo(CONNECTOR_ID1, new TestHsqlConnectionInfo());
    _connectorRepository.addConnectionInfo(CONNECTOR_ID2, new TestDerbyConnectionInfo());
  }

  @Test
  public void testNoTables() throws Exception {
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");

    final SchemaCompatibilityIssues compatibilityIssues = _objectUnderTest.check(CONNECTOR_ID1, CONNECTOR_ID2);
    Assert.assertTrue(compatibilityIssues.isSevere());
    Assert.assertNotNull(compatibilityIssues.contains(SchemaCompatibilityIssueType.MISSING_TABLE));
  }

  @Test
  public void testMissingTable() throws Exception {
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, "/ddl/tables-missingTable.sql");

    final SchemaCompatibilityIssues compatibilityIssues = _objectUnderTest.check(CONNECTOR_ID1, CONNECTOR_ID2);
    Assert.assertTrue(compatibilityIssues.isSevere());
    Assert.assertNotNull(compatibilityIssues.contains(SchemaCompatibilityIssueType.MISSING_TABLE));
  }

  @Test
  public void testDuplicateIndex() throws Exception {
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, "/ddl/tables-duplicate-index.sql");

    final SchemaCompatibilityIssues compatibilityIssues = _objectUnderTest.check(CONNECTOR_ID1, CONNECTOR_ID2);
    Assert.assertFalse(compatibilityIssues.isSevere());
    Assert.assertNotNull(compatibilityIssues.contains(SchemaCompatibilityIssueType.DUPLICATE_INDEX));
  }


  @Test
  public void testDuplicateForeignKey() throws Exception {
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, "/ddl/tables-duplicate-foreignkey.sql");

    final SchemaCompatibilityIssues compatibilityIssues = _objectUnderTest.check(CONNECTOR_ID1, CONNECTOR_ID2);
    Assert.assertFalse(compatibilityIssues.isSevere());
    Assert.assertNotNull(compatibilityIssues.contains(SchemaCompatibilityIssueType.DUPLICATE_FOREIGN_KEY));
  }

  @Test
  public void testAdditionalColumn() throws Exception {
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, "/ddl/tables-additional-column.sql");

    final SchemaCompatibilityIssues compatibilityIssues = _objectUnderTest.check(CONNECTOR_ID1, CONNECTOR_ID2);
    Assert.assertTrue(compatibilityIssues.isSevere());
    Assert.assertNotNull(compatibilityIssues.contains(SchemaCompatibilityIssueType.ADDITIONAL_NONNULL_COLUMN));
  }

  @Test
  public void testMissingColumn() throws Exception {
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID1, "/ddl/tables.sql");
    new ScriptExecutorTool(_connectorRepository).executeFileScript(CONNECTOR_ID2, "/ddl/tables-incompatibleColumn.sql");

    final SchemaCompatibilityIssues compatibilityIssues = _objectUnderTest.check(CONNECTOR_ID1, CONNECTOR_ID2);
    Assert.assertTrue(compatibilityIssues.isSevere());
    Assert.assertNotNull(compatibilityIssues.contains(SchemaCompatibilityIssueType.MISSING_COLUMN));
    Assert.assertNotNull(compatibilityIssues.contains(SchemaCompatibilityIssueType.INCOMPATIBLE_COLUMNS));
    final SchemaCompatibilityIssue missingForeignKeyIssue = compatibilityIssues.contains(SchemaCompatibilityIssueType.MISSING_FOREIGN_KEY);
    Assert.assertNotNull(missingForeignKeyIssue);

    final ForeignKeyMetaData foreignKeyMetaData = ((MissingForeignKeyIssue) missingForeignKeyIssue).getForeignKeyMetaData();
    Assert.assertEquals("ASSIGNED_COMPANY_ID", foreignKeyMetaData.getReferencingColumn().getColumnName());

    final SchemaCompatibilityIssue missingIndexIssue = compatibilityIssues.contains(SchemaCompatibilityIssueType.MISSING_INDEX);
    Assert.assertNotNull(missingIndexIssue);
    Assert.assertEquals("COMPANY_NAME_IDX", ((MissingIndexIssue) missingIndexIssue).getIndexMetaData().getIndexName());
  }
}
