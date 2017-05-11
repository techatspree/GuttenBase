package de.akquinet.jbosscc.guttenbase.tools.schema;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Create Custom DDL from existing schema
 *
 * @copyright akquinet tech@spree GmbH, 2002-2020
 */
@SuppressWarnings("SameParameterValue")
public class CopySchemaTool
{
  private final ConnectorRepository _connectorRepository;
  private final int _maxIdLength;


  public CopySchemaTool(final ConnectorRepository connectorRepository, final int maxIdLength) {
    assert connectorRepository != null : "connectorRepository != null";

    _connectorRepository = connectorRepository;
    _maxIdLength = maxIdLength;
  }

  public CopySchemaTool(final ConnectorRepository connectorRepository) {
    this(connectorRepository, SchemaScriptCreatorTool.MAX_ID_LENGTH);
  }

  public List<String> createDDLScript(final String sourceConnectorId, final String targetConnectorId) throws SQLException {
    final List<String> result = new ArrayList<>();
    final SchemaScriptCreatorTool schemaScriptCreatorTool = new SchemaScriptCreatorTool(_connectorRepository, sourceConnectorId,
      targetConnectorId, _maxIdLength);

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

