package de.akquinet.jbosscc.guttenbase.tools.schema;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;

/**
 * Create DDL from existing schema
 * 
 * @copyright akquinet tech@spree GmbH, 2002-2020
 */
public class CreateSchemaTool
{
  private final ConnectorRepository _connectorRepository;
  private final SchemaColumnTypeMapper _schemaColumnTypeMapper;
  private final CaseConversionMode _caseConversionMode;

  public CreateSchemaTool(
      final ConnectorRepository connectorRepository,
      final SchemaColumnTypeMapper schemaColumnTypeMapper,
      final CaseConversionMode caseConversionMode)
  {
    assert caseConversionMode != null : "caseConversionMode != null";
    assert connectorRepository != null : "connectorRepository != null";
    assert schemaColumnTypeMapper != null : "schemaColumnTypeMapper != null";

    _connectorRepository = connectorRepository;
    _schemaColumnTypeMapper = schemaColumnTypeMapper;
    _caseConversionMode = caseConversionMode;
  }

  public CreateSchemaTool(final ConnectorRepository connectorRepository)
  {
    this(connectorRepository, new DefaultSchemaColumnTypeMapper(), CaseConversionMode.NONE);
  }

  public List<String> createDDLScript(final String connectorId, final String schema) throws SQLException
  {
    final List<String> result = new ArrayList<String>();
    final DatabaseMetaData databaseMetaData = _connectorRepository.getDatabaseMetaData(connectorId);

    final DatabaseSchemaScriptCreator databaseSchemaScriptCreator = new DatabaseSchemaScriptCreator(databaseMetaData, schema,
        _caseConversionMode);
    databaseSchemaScriptCreator.setColumnTypeMapper(_schemaColumnTypeMapper);
    result.addAll(databaseSchemaScriptCreator.createTableStatements());
    result.addAll(databaseSchemaScriptCreator.createPrimaryKeyStatements());
    result.addAll(databaseSchemaScriptCreator.createForeignKeyStatements());
    result.addAll(databaseSchemaScriptCreator.createIndexStatements());

    return result;
  }

  public void copySchema(final String sourceConnectorId, final String targetConnectorId) throws SQLException
  {
    final DatabaseMetaData databaseMetaData = _connectorRepository.getDatabaseMetaData(targetConnectorId);

    final List<String> ddlScript = createDDLScript(sourceConnectorId, databaseMetaData.getSchema());

    new ScriptExecutorTool(_connectorRepository).executeScript(targetConnectorId, ddlScript);
  }
}
