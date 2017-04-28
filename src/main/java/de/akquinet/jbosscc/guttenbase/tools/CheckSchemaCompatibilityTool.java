package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssues;
import java.sql.SQLException;

/**
 * Will check two schema's tables for compatibility.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 * @gb.UsesHint {@link ColumnNameMapperHint} to map column names
 * @gb.UsesHint {@link TableOrderHint} to determine order of tables
 * @deprecated Use SchemaComparatorTool
 */
@SuppressWarnings("DeprecatedIsStillUsed")
public class CheckSchemaCompatibilityTool extends SchemaComparatorTool {

  public CheckSchemaCompatibilityTool(final ConnectorRepository connectorRepository) {
    super(connectorRepository);
  }

    /**
     * Check compatibility of both connectors/schemata.
     * @param sourceConnectorId
     * @param targetConnectorId
     * @throws SQLException
     */


  @SuppressWarnings("JavaDoc")
  public void checkTableConfiguration(final String sourceConnectorId, final String targetConnectorId) throws SQLException {
    final SchemaCompatibilityIssues schemaCompatibilityIssues = check(sourceConnectorId, targetConnectorId);

    if (schemaCompatibilityIssues.isSevere()) {
      throw new SQLException(schemaCompatibilityIssues.toString());
    }
  }
}
