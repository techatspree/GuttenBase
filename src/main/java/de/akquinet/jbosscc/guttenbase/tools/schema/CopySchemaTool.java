package de.akquinet.jbosscc.guttenbase.tools.schema;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Create Custom DDL from existing schema
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 */
@SuppressWarnings("SameParameterValue")
public class CopySchemaTool {
  private final ConnectorRepository _connectorRepository;

  public CopySchemaTool(final ConnectorRepository connectorRepository) {
    assert connectorRepository != null : "connectorRepository != null";

    _connectorRepository = connectorRepository;
  }

  public List<String> createDDLScript(final String sourceConnectorId, final String targetConnectorId) throws SQLException {
    final List<String> result = new ArrayList<>();
    final SchemaScriptCreatorTool schemaScriptCreatorTool = new SchemaScriptCreatorTool(_connectorRepository, sourceConnectorId,
      targetConnectorId);

    result.addAll(schemaScriptCreatorTool.createTableStatements());
    result.addAll(schemaScriptCreatorTool.createPrimaryKeyStatements());
    result.addAll(schemaScriptCreatorTool.createForeignKeyStatements());
    result.addAll(schemaScriptCreatorTool.createIndexStatements());

    return result;
  }

  public void copySchema(final String sourceConnectorId, final String targetConnectorId) throws SQLException {
    final List<String> ddlScript = createDDLScript(sourceConnectorId, targetConnectorId);
    new ScriptExecutorTool(_connectorRepository).executeScript(targetConnectorId, ddlScript);
  }
}

