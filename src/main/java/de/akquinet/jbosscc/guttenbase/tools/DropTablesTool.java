package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.connector.ConnectorInfo;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.hints.TableOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;
import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Will drop tables in given schema. USE WITH CARE!
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 * @gb.UsesHint {@link TableOrderHint} to determine order of tables
 */
public class DropTablesTool {
    private static final String DEFAULT_INDEX_DROP = "DROP INDEX @@FULL_INDEX_NAME@@;";
    private static final String POSTGRES_CONSTRAINT_DROP = "ALTER TABLE @@FULL_TABLE_NAME@@ DROP CONSTRAINT @@INDEX_NAME@@;";

    final ConnectorRepository _connectorRepository;

    public DropTablesTool(final ConnectorRepository connectorRepository) {
        assert connectorRepository != null : "connectorRepository != null";
        _connectorRepository = connectorRepository;
    }

    public List<String> createDropForeignKeyStatements(final String connectorId) throws SQLException {
        final List<TableMetaData> tableMetaData = new TableOrderTool().getOrderedTables(
            TableOrderHint.getSortedTables(_connectorRepository, connectorId), false);
        final TableMapper tableMapper = _connectorRepository.getConnectorHint(connectorId, TableMapper.class).getValue();
        final List<String> statements = new ArrayList<>();
        final ConnectorInfo connectionInfo = _connectorRepository.getConnectionInfo(connectorId);
        final String constraintClause;

        switch (connectionInfo.getDatabaseType()) {
            case MYSQL:
                constraintClause = " FOREIGN KEY ";
                break;
            default:
                constraintClause = " CONSTRAINT ";
                break;
        }

        for (final TableMetaData table : tableMetaData) {
            for (final ForeignKeyMetaData foreignKey : table.getImportedForeignKeys()) {
                statements.add("ALTER TABLE " + tableMapper.fullyQualifiedTableName(table, table.getDatabaseMetaData())
                    + " DROP"
                    + constraintClause
                    + foreignKey.getForeignKeyName()
                    + ";");
            }
        }

        return statements;
    }

    public List<String> createDropIndexesStatements(final String connectorId) throws SQLException {
        final List<TableMetaData> tableMetaData = new TableOrderTool().getOrderedTables(
            TableOrderHint.getSortedTables(_connectorRepository, connectorId), false);
        final List<String> statements = new ArrayList<>();
        final ConnectorInfo connectionInfo = _connectorRepository.getConnectionInfo(connectorId);
        final TableMapper tableMapper = _connectorRepository.getConnectorHint(connectorId, TableMapper.class).getValue();

        for (final TableMetaData table : tableMetaData) {
            final String schemaPrefix = table.getDatabaseMetaData().getSchemaPrefix();
            final String fullTableName = tableMapper.fullyQualifiedTableName(table, table.getDatabaseMetaData());

            for (final IndexMetaData index : table.getIndexes()) {
                if (!index.isPrimaryKeyIndex()) {
                    final String fullIndexName = schemaPrefix + index.getIndexName();

                    String constraintClause = DEFAULT_INDEX_DROP;

                    if (connectionInfo.getDatabaseType() == DatabaseType.POSTGRESQL && index.isUnique()) {
                        constraintClause = POSTGRES_CONSTRAINT_DROP;
                    }

                    statements.add(constraintClause
                        .replaceAll("@@INDEX_NAME@@", index.getIndexName())
                        .replaceAll("@@FULL_INDEX_NAME@@", fullIndexName)
                        .replaceAll("@@FULL_TABLE_NAME@@", fullTableName));
                }
            }

        }

        return statements;
    }

    public List<String> createDropTableStatements(final String connectorId) throws SQLException {
        return createTableStatements(connectorId, "DROP TABLE");
    }

    public List<String> createDeleteTableStatements(final String connectorId) throws SQLException {
        return createTableStatements(connectorId, "DELETE FROM");
    }

    public void dropTables(final String targetId) throws SQLException {
        new ScriptExecutorTool(_connectorRepository).executeScript(targetId, true, true, createDropTableStatements(targetId));
    }

    public void clearTables(final String targetId) throws SQLException {
        new ScriptExecutorTool(_connectorRepository).executeScript(targetId, true, true, createDeleteTableStatements(targetId));
    }

    public void dropIndexes(final String targetId) throws SQLException {
        new ScriptExecutorTool(_connectorRepository).executeScript(targetId, true, false, createDropIndexesStatements(targetId));
    }

    public void dropForeignKeys(final String targetId) throws SQLException {
        new ScriptExecutorTool(_connectorRepository).executeScript(targetId, true, false, createDropForeignKeyStatements(targetId));
    }

    private List<String> createTableStatements(final String connectorId, final String clausePrefix) throws SQLException {
        final List<TableMetaData> tableMetaData = new TableOrderTool().getOrderedTables(
          TableOrderHint.getSortedTables(_connectorRepository, connectorId), false);
        final List<String> statements = new ArrayList<>();
        final TableMapper tableMapper = _connectorRepository.getConnectorHint(connectorId, TableMapper.class).getValue();

        for (final TableMetaData table : tableMetaData) {
            statements.add(clausePrefix + " " + tableMapper.fullyQualifiedTableName(table, table.getDatabaseMetaData()) + ";");
        }

        return statements;
    }
}
