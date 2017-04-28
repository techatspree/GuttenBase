package de.akquinet.jbosscc.guttenbase.tools.schema;

import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultColumnMapper;
import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultTableMapper;
import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Create DDL from existing schema
 *
 * @copyright akquinet tech@spree GmbH, 2002-2020
 */
public class CreateSchemaTool {
  private final ConnectorRepository _connectorRepository;
  private final int _maxIdLength;

  private SchemaColumnTypeMapper _columnTypeMapper = new DefaultSchemaColumnTypeMapper();
  private ColumnMapper _columnMapper = new DefaultColumnMapper(CaseConversionMode.UPPER);
  private TableMapper _tableMapper = new DefaultTableMapper(CaseConversionMode.UPPER);

  public CreateSchemaTool(final ConnectorRepository connectorRepository,
                          final int maxIdLength) {
    assert connectorRepository != null : "connectorRepository != null";

    _connectorRepository = connectorRepository;
    _maxIdLength = maxIdLength;
  }

  public CreateSchemaTool(final ConnectorRepository connectorRepository) {
    this(connectorRepository, DatabaseSchemaScriptCreator.MAX_ID_LENGTH);
  }

  public List<String> createDDLScript(final String sourceConnectorId, final DatabaseMetaData targetDatabaseMetaData) throws
    SQLException {
    final List<String> result = new ArrayList<>();
    final DatabaseMetaData sourceDatabaseMetaData = _connectorRepository.getDatabaseMetaData(sourceConnectorId);

    final DatabaseSchemaScriptCreator databaseSchemaScriptCreator = new DatabaseSchemaScriptCreator(sourceDatabaseMetaData,
      targetDatabaseMetaData, _maxIdLength);
    databaseSchemaScriptCreator.setColumnTypeMapper(_columnTypeMapper);
    databaseSchemaScriptCreator.setColumnMapper(_columnMapper);
    databaseSchemaScriptCreator.setTableMapper(_tableMapper);

    result.addAll(databaseSchemaScriptCreator.createTableStatements());
    result.addAll(databaseSchemaScriptCreator.createPrimaryKeyStatements());
    result.addAll(databaseSchemaScriptCreator.createForeignKeyStatements());
    result.addAll(databaseSchemaScriptCreator.createIndexStatements());

    return result;
  }

  public void copySchema(final String sourceConnectorId, final String targetConnectorId) throws SQLException {
    final DatabaseMetaData databaseMetaData = _connectorRepository.getDatabaseMetaData(targetConnectorId);

    final List<String> ddlScript = createDDLScript(sourceConnectorId, databaseMetaData);

    new ScriptExecutorTool(_connectorRepository).executeScript(targetConnectorId, ddlScript);
  }

  public void setTableMapper(final TableMapper tableMapper) {
    _tableMapper = tableMapper;
  }

  public void setColumnMapper(final ColumnMapper columnMapper) {
    assert columnMapper != null : "columnNameMapper != null";
    _columnMapper = columnMapper;
  }

  public void setColumnTypeMapper(final SchemaColumnTypeMapper columnTypeMapper) {
    assert columnTypeMapper != null : "columnTypeMapper != null";
    _columnTypeMapper = columnTypeMapper;
  }
}
