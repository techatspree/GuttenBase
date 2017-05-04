package de.akquinet.jbosscc.guttenbase.tools.schema;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.ScriptExecutorTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Create Custom DDL from existing schema
 *
 * @copyright akquinet tech@spree GmbH, 2002-2020
 */
@SuppressWarnings("SameParameterValue")
public class CreateCustomSchemaTool

{
    private final ConnectorRepository _connectorRepository;
    private final int _maxIdLength;


    public CreateCustomSchemaTool(final ConnectorRepository connectorRepository, final int maxIdLength)
    {
        assert connectorRepository != null : "connectorRepository != null";

        _connectorRepository = connectorRepository;
        _maxIdLength = maxIdLength;
    }

    public CreateCustomSchemaTool(final ConnectorRepository connectorRepository)
    {
        this(connectorRepository, DatabaseSchemaScriptCreator.MAX_ID_LENGTH);
    }

    public List<String> createDDLScript(final String connectorId, final String targetConnectorId) throws Exception
    {
        final List<String> result = new ArrayList<>();
        final DatabaseCustomSchemaScriptCreator databaseCustomSchemaScriptCreator = new DatabaseCustomSchemaScriptCreator(_connectorRepository, connectorId,
                targetConnectorId, _maxIdLength);

        result.addAll( databaseCustomSchemaScriptCreator.createTableStatements());
        result.addAll( databaseCustomSchemaScriptCreator.createPrimaryKeyStatements());
        result.addAll( databaseCustomSchemaScriptCreator.createForeignKeyStatements());
        result.addAll( databaseCustomSchemaScriptCreator.createIndexStatements());

        return result;
    }

    public void copySchema(final String sourceConnectorId, final String targetConnectorId) throws Exception
    {
        final List<String> ddlScript = createDDLScript(sourceConnectorId, targetConnectorId);
        new ScriptExecutorTool(_connectorRepository).executeScript(targetConnectorId, ddlScript);
    }
}

