package de.akquinet.jbosscc.guttenbase.tools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.utils.DatabaseSchemaScriptCreator;

/**
 * Create DDL from existing schema
 * 
 * @copyright akquinet tech@spree GmbH, 2002-2020
 */
public class CreateSchemaTool
{
  private final ConnectorRepository _connectorRepository;

  public CreateSchemaTool(final ConnectorRepository connectorRepository)
  {
    assert connectorRepository != null : "connectorRepository != null";
    _connectorRepository = connectorRepository;
  }

  public List<String> createDDLScript(final String connectorId, final String schema) throws SQLException
  {
    final List<String> result = new ArrayList<String>();
    final DatabaseMetaData databaseMetaData = _connectorRepository.getDatabaseMetaData(connectorId);

    final DatabaseSchemaScriptCreator databaseSchemaScriptCreator = new DatabaseSchemaScriptCreator(databaseMetaData, schema);
    result.addAll(databaseSchemaScriptCreator.createTableStatements());
    result.addAll(databaseSchemaScriptCreator.createPrimaryKeyStatements());
    result.addAll(databaseSchemaScriptCreator.createForeignKeyStatements());
    result.addAll(databaseSchemaScriptCreator.createIndexStatements());

    return result;
  }
}
