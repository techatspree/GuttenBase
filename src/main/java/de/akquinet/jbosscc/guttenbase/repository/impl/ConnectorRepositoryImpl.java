package de.akquinet.jbosscc.guttenbase.repository.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.akquinet.jbosscc.guttenbase.configuration.SourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.TargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.Db2SourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.Db2TargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.DerbySourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.DerbyTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.GenericSourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.GenericTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.H2DbSourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.H2DbTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.HsqldbSourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.HsqldbTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.MsAccessSourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.MsAccessTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.MsSqlSourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.MsSqlTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.MySqlSourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.MySqlTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.OracleSourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.OracleTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.PostgresqlSourceDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.configuration.impl.PostgresqlTargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.connector.Connector;
import de.akquinet.jbosscc.guttenbase.connector.ConnectorInfo;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.export.ExportDumpDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.export.ImportDumpDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.hints.ConnectorHint;
import de.akquinet.jbosscc.guttenbase.hints.RepositoryTableFilterHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultColumnDataMapperProviderHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultColumnMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultColumnNameMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultColumnOrderHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultColumnTypeResolverListHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultDatabaseTableFilterHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultEntityTableCheckerHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultExportDumpExtraInformationHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultExporterFactoryHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultImportDumpExtraInformationHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultImporterFactoryHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultMaxNumberOfDataItemsHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultNumberOfCheckedTableDataHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultNumberOfRowsPerBatchHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultProgressIndicatorHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultRepositoryColumnFilterHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultRepositoryTableFilterHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultSplitColumnHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultTableMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultTableNameMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultTableOrderHint;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultZipExporterClassResourcesHint;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalDatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalTableMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryColumnFilter;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryTableFilter;
import de.akquinet.jbosscc.guttenbase.utils.Util;

/**
 * The main repository containing all configured connectors.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @Uses-Hint {@link RepositoryTableFilterHint} when returning table meta data
 * @author M. Dahm
 */
public class ConnectorRepositoryImpl implements ConnectorRepository
{
  private static final long serialVersionUID = 1L;

  private final Map<String, ConnectorInfo> _connectionInfoMap = new TreeMap<String, ConnectorInfo>();
  private final Map<DatabaseType, SourceDatabaseConfiguration> _sourceDatabaseConfigurationMap = new HashMap<DatabaseType, SourceDatabaseConfiguration>();
  private final Map<DatabaseType, TargetDatabaseConfiguration> _targetDatabaseConfigurationMap = new HashMap<DatabaseType, TargetDatabaseConfiguration>();

  /**
   * Hash meta data since some data base are very slow on retrieving it.
   */
  private final Map<String, DatabaseMetaData> _databaseMetaDataMap = new HashMap<String, DatabaseMetaData>();
  private final Map<String, Map<Class<?>, ConnectorHint<?>>> _connectionHintMap = new HashMap<String, Map<Class<?>, ConnectorHint<?>>>();

  public ConnectorRepositoryImpl()
  {
    initDefaultConfiguration();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addConnectionInfo(final String connectorId, final ConnectorInfo connectionInfo)
  {
    assert connectorId != null : "connectorId != null";
    assert connectionInfo != null : "connectionInfo != null";
    _connectionInfoMap.put(connectorId, connectionInfo);

    initDefaultHints(connectorId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeConnectionInfo(final String connectorId)
  {
    assert connectorId != null : "connectorId != null";

    _connectionInfoMap.remove(connectorId);
    _connectionHintMap.remove(connectorId);
    _databaseMetaDataMap.remove(connectorId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void addConnectorHint(final String connectorId, final ConnectorHint<T> hint)
  {
    assert connectorId != null : "connectorId != null";
    assert hint != null : "hint != null";

    // Check connector if is configured
    getConnectionInfo(connectorId);

    Map<Class<?>, ConnectorHint<?>> hintMap = _connectionHintMap.get(connectorId);

    if (hintMap == null)
    {
      hintMap = new HashMap<Class<?>, ConnectorHint<?>>();
      _connectionHintMap.put(connectorId, hintMap);
    }

    hintMap.put(hint.getConnectorHintType(), hint);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void removeConnectorHint(final String connectorId, final Class<T> connectionInfoHintType)
  {
    assert connectorId != null : "connectorId != null";
    assert connectionInfoHintType != null : "connectionInfoHintType != null";

    final Map<Class<?>, ConnectorHint<?>> hintMap = _connectionHintMap.get(connectorId);

    if (hintMap != null)
    {
      hintMap.remove(connectionInfoHintType);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T> ConnectorHint<T> getConnectorHint(final String connectorId, final Class<T> connectionInfoHintType)
  {
    assert connectorId != null : "connectorId != null";
    assert connectionInfoHintType != null : "connectionInfoHintType != null";

    final Map<Class<?>, ConnectorHint<?>> hintMap = _connectionHintMap.get(connectorId);

    if (hintMap == null)
    {
      throw new IllegalStateException("No hints defined for " + connectorId);
    }

    return (ConnectorHint<T>) hintMap.get(connectionInfoHintType);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConnectorInfo getConnectionInfo(final String connectorId)
  {
    assert connectorId != null : "connectorId != null";

    final ConnectorInfo connectionInfo = _connectionInfoMap.get(connectorId);

    if (connectionInfo == null)
    {
      throw new IllegalStateException("Connector not configured: " + connectorId);
    }

    return connectionInfo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DatabaseMetaData getDatabaseMetaData(final String connectorId) throws SQLException
  {
    assert connectorId != null : "connectorId != null";

    DatabaseMetaData databaseMetaData = _databaseMetaDataMap.get(connectorId);

    if (databaseMetaData == null)
    {
      final Connector connector = createConnector(connectorId);
      databaseMetaData = connector.retrieveDatabaseMetaData();
      _databaseMetaDataMap.put(connectorId, databaseMetaData);
    }

    return createResultWithFilteredTables(connectorId, databaseMetaData);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshDatabaseMetaData(final String connectorId)
  {
    assert connectorId != null : "connectorId != null";

    _databaseMetaDataMap.remove(connectorId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Connector createConnector(final String connectorId)
  {
    assert connectorId != null : "connectorId != null";

    final ConnectorInfo connectionInfo = getConnectionInfo(connectorId);

    return connectionInfo.createConnector(this, connectorId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SourceDatabaseConfiguration getSourceDatabaseConfiguration(final String connectorId)
  {
    assert connectorId != null : "connectorId != null";

    final ConnectorInfo connectionInfo = getConnectionInfo(connectorId);
    final DatabaseType databaseType = connectionInfo.getDatabaseType();
    final SourceDatabaseConfiguration result = _sourceDatabaseConfigurationMap.get(databaseType);

    if (result == null)
    {
      throw new IllegalStateException("Unhandled source connector data base type: " + databaseType);
    }
    else
    {
      return result;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addSourceDatabaseConfiguration(final DatabaseType databaseType,
      final SourceDatabaseConfiguration sourceDatabaseConfiguration)
  {
    assert databaseType != null : "databaseType != null";
    assert sourceDatabaseConfiguration != null : "sourceDatabaseConfiguration != null";
    _sourceDatabaseConfigurationMap.put(databaseType, sourceDatabaseConfiguration);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addTargetDatabaseConfiguration(final DatabaseType databaseType,
      final TargetDatabaseConfiguration targetDatabaseConfiguration)
  {
    assert targetDatabaseConfiguration != null : "targetDatabaseConfiguration != null";
    assert databaseType != null : "databaseType != null";

    _targetDatabaseConfigurationMap.put(databaseType, targetDatabaseConfiguration);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TargetDatabaseConfiguration getTargetDatabaseConfiguration(final String connectorId)
  {
    assert connectorId != null : "connectorId != null";

    final ConnectorInfo connectionInfo = getConnectionInfo(connectorId);
    final DatabaseType databaseType = connectionInfo.getDatabaseType();
    final TargetDatabaseConfiguration result = _targetDatabaseConfigurationMap.get(databaseType);

    if (result == null)
    {
      throw new IllegalStateException("Unhandled target connector data base type: " + databaseType);
    }
    else
    {
      return result;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getConnectorIds()
  {
    return new ArrayList<String>(_connectionInfoMap.keySet());
  }

  private DatabaseMetaData createResultWithFilteredTables(final String connectorId, final DatabaseMetaData databaseMetaData)
      throws SQLException
  {
    final InternalDatabaseMetaData resultDatabaseMetaData = Util.copyObject(InternalDatabaseMetaData.class,
        (InternalDatabaseMetaData) databaseMetaData);
    final RepositoryTableFilter tableFilter = getConnectorHint(connectorId, RepositoryTableFilter.class).getValue();
    final RepositoryColumnFilter columnFilter = getConnectorHint(connectorId, RepositoryColumnFilter.class).getValue();

    for (final TableMetaData tableMetaData : resultDatabaseMetaData.getTableMetaData())
    {
      if (tableFilter.accept(tableMetaData))
      {
        for (final ColumnMetaData columnMetaData : tableMetaData.getColumnMetaData())
        {
          if (!columnFilter.accept(columnMetaData))
          {
            ((InternalTableMetaData) tableMetaData).removeColumn(columnMetaData);
          }
        }

      }
      else
      {
        resultDatabaseMetaData.removeTableMetaData(tableMetaData);
      }
    }

    return resultDatabaseMetaData;
  }

  private void initDefaultConfiguration()
  {
    addSourceDatabaseConfiguration(DatabaseType.GENERIC, new GenericSourceDatabaseConfiguration(this));
    addSourceDatabaseConfiguration(DatabaseType.MOCK, new GenericSourceDatabaseConfiguration(this));
    addSourceDatabaseConfiguration(DatabaseType.DB2, new Db2SourceDatabaseConfiguration(this));
    addSourceDatabaseConfiguration(DatabaseType.MSSQL, new MsSqlSourceDatabaseConfiguration(this));
    addSourceDatabaseConfiguration(DatabaseType.MYSQL, new MySqlSourceDatabaseConfiguration(this));
    addSourceDatabaseConfiguration(DatabaseType.POSTGRESQL, new PostgresqlSourceDatabaseConfiguration(this));
    addSourceDatabaseConfiguration(DatabaseType.ORACLE, new OracleSourceDatabaseConfiguration(this));
    addSourceDatabaseConfiguration(DatabaseType.EXPORT_DUMP, new ImportDumpDatabaseConfiguration(this));
    addSourceDatabaseConfiguration(DatabaseType.IMPORT_DUMP, new ImportDumpDatabaseConfiguration(this));
    addSourceDatabaseConfiguration(DatabaseType.HSQLDB, new HsqldbSourceDatabaseConfiguration(this));
    addSourceDatabaseConfiguration(DatabaseType.H2DB, new H2DbSourceDatabaseConfiguration(this));
    addSourceDatabaseConfiguration(DatabaseType.DERBY, new DerbySourceDatabaseConfiguration(this));
    addSourceDatabaseConfiguration(DatabaseType.MS_ACCESS, new MsAccessSourceDatabaseConfiguration(this));

    addTargetDatabaseConfiguration(DatabaseType.GENERIC, new GenericTargetDatabaseConfiguration(this));
    addTargetDatabaseConfiguration(DatabaseType.MOCK, new GenericTargetDatabaseConfiguration(this));
    addTargetDatabaseConfiguration(DatabaseType.DB2, new Db2TargetDatabaseConfiguration(this));
    addTargetDatabaseConfiguration(DatabaseType.MSSQL, new MsSqlTargetDatabaseConfiguration(this));
    addTargetDatabaseConfiguration(DatabaseType.MYSQL, new MySqlTargetDatabaseConfiguration(this));
    addTargetDatabaseConfiguration(DatabaseType.ORACLE, new OracleTargetDatabaseConfiguration(this));
    addTargetDatabaseConfiguration(DatabaseType.POSTGRESQL, new PostgresqlTargetDatabaseConfiguration(this));
    addTargetDatabaseConfiguration(DatabaseType.EXPORT_DUMP, new ExportDumpDatabaseConfiguration(this));
    addTargetDatabaseConfiguration(DatabaseType.IMPORT_DUMP, new ExportDumpDatabaseConfiguration(this));
    addTargetDatabaseConfiguration(DatabaseType.HSQLDB, new HsqldbTargetDatabaseConfiguration(this));
    addTargetDatabaseConfiguration(DatabaseType.H2DB, new H2DbTargetDatabaseConfiguration(this));
    addTargetDatabaseConfiguration(DatabaseType.DERBY, new DerbyTargetDatabaseConfiguration(this));
    addTargetDatabaseConfiguration(DatabaseType.MS_ACCESS, new MsAccessTargetDatabaseConfiguration(this));
  }

  private void initDefaultHints(final String connectorId)
  {
    addConnectorHint(connectorId, new DefaultRepositoryTableFilterHint());
    addConnectorHint(connectorId, new DefaultDatabaseTableFilterHint());
    addConnectorHint(connectorId, new DefaultNumberOfRowsPerBatchHint());
    addConnectorHint(connectorId, new DefaultNumberOfCheckedTableDataHint());
    addConnectorHint(connectorId, new DefaultMaxNumberOfDataItemsHint());
    addConnectorHint(connectorId, new DefaultSplitColumnHint());
    addConnectorHint(connectorId, new DefaultColumnTypeResolverListHint());
    addConnectorHint(connectorId, new DefaultColumnNameMapperHint());
    addConnectorHint(connectorId, new DefaultEntityTableCheckerHint());
    addConnectorHint(connectorId, new DefaultTableNameMapperHint());
    addConnectorHint(connectorId, new DefaultExporterFactoryHint());
    addConnectorHint(connectorId, new DefaultImporterFactoryHint());
    addConnectorHint(connectorId, new DefaultZipExporterClassResourcesHint());
    addConnectorHint(connectorId, new DefaultColumnDataMapperProviderHint());
    addConnectorHint(connectorId, new DefaultTableOrderHint());
    addConnectorHint(connectorId, new DefaultColumnOrderHint());
    addConnectorHint(connectorId, new DefaultTableMapperHint());
    addConnectorHint(connectorId, new DefaultColumnMapperHint());
    addConnectorHint(connectorId, new DefaultRepositoryColumnFilterHint());
    addConnectorHint(connectorId, new DefaultExportDumpExtraInformationHint());
    addConnectorHint(connectorId, new DefaultImportDumpExtraInformationHint());
    addConnectorHint(connectorId, new DefaultProgressIndicatorHint());
  }
}
