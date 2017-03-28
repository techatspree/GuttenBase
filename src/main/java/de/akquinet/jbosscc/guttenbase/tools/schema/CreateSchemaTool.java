package de.akquinet.jbosscc.guttenbase.tools.schema;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultTableNameMapper;
import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnNameMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
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
  private final int _maxIdLength;

  private SchemaColumnTypeMapper _columnTypeMapper = new DefaultSchemaColumnTypeMapper();
  private ColumnNameMapper _columnNameMapper = new DefaultColumnNameMapper(CaseConversionMode.UPPER);
  private TableNameMapper _tableNameMapper = new DefaultTableNameMapper(CaseConversionMode.UPPER, false);

  public CreateSchemaTool(final ConnectorRepository connectorRepository,
      final int maxIdLength)
  {
    assert connectorRepository != null : "connectorRepository != null";

    _connectorRepository = connectorRepository;
    _maxIdLength = maxIdLength;
  }

  public CreateSchemaTool(final ConnectorRepository connectorRepository)
  {
    this(connectorRepository, DatabaseSchemaScriptCreator.MAX_ID_LENGTH);
  }

  public List<String> createDDLScript(final String connectorId, final String targetSchema) throws SQLException
  {
    final List<String> result = new ArrayList<>();
    final DatabaseMetaData databaseMetaData = _connectorRepository.getDatabaseMetaData(connectorId);

    final DatabaseSchemaScriptCreator databaseSchemaScriptCreator = new DatabaseSchemaScriptCreator(databaseMetaData,
        targetSchema, _maxIdLength);
    databaseSchemaScriptCreator.setColumnTypeMapper(_columnTypeMapper);
    databaseSchemaScriptCreator.setColumnNameMapper(_columnNameMapper);
    databaseSchemaScriptCreator.setTableNameMapper(_tableNameMapper);

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

  public void setTableNameMapper(final TableNameMapper tableNameMapper)
  {
    _tableNameMapper = tableNameMapper;
  }

  public void setColumnNameMapper(final ColumnNameMapper columnNameMapper)
  {
    assert columnNameMapper != null : "columnNameMapper != null";
    _columnNameMapper = columnNameMapper;
  }

  public void setColumnTypeMapper(final SchemaColumnTypeMapper columnTypeMapper)
  {
    assert columnTypeMapper != null : "columnTypeMapper != null";
    _columnTypeMapper = columnTypeMapper;
  }
}
