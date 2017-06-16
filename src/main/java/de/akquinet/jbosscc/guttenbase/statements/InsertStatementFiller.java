package de.akquinet.jbosscc.guttenbase.statements;

import de.akquinet.jbosscc.guttenbase.configuration.TargetDatabaseConfiguration;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.exceptions.IncompatibleColumnsException;
import de.akquinet.jbosscc.guttenbase.exceptions.MissingDataException;
import de.akquinet.jbosscc.guttenbase.hints.ColumnOrderHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper.ColumnMapperResult;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapping;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.CommonColumnTypeResolverTool;
import org.apache.log4j.Logger;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Fill previously created INSERT statement with data from source connector.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 * @gb.UsesHint {@link ColumnOrderHint} to determine column order
 */
public class InsertStatementFiller
{
  private static final Logger LOG = Logger.getLogger(InsertStatementFiller.class);

  private final ConnectorRepository _connectorRepository;
  private final List<Closeable> _closeableObjects = new ArrayList<>();

  public InsertStatementFiller(final ConnectorRepository connectorRepository)
  {
    assert connectorRepository != null : "connectorRepository != null";
    _connectorRepository = connectorRepository;
  }

  public void fillInsertStatementFromResultSet(final String sourceConnectorId, final TableMetaData sourceTableMetaData,
                                               final String targetConnectorId, final TableMetaData targetTableMetaData,
                                               final TargetDatabaseConfiguration targetDatabaseConfiguration, final Connection targetConnection, final ResultSet rs,
                                               final PreparedStatement insertStatement, final int numberOfRowsPerBatch, final boolean useMultipleValuesClauses)
          throws SQLException
  {
    final CommonColumnTypeResolverTool commonColumnTypeResolver = new CommonColumnTypeResolverTool(_connectorRepository);
    final List<ColumnMetaData> sourceColumns = ColumnOrderHint.getSortedColumns(_connectorRepository, sourceConnectorId,
            sourceTableMetaData);
    final ColumnMapper columnMapper = _connectorRepository.getConnectorHint(targetConnectorId, ColumnMapper.class).getValue();
    final DatabaseType targetDatabaseType = targetTableMetaData.getDatabaseMetaData().getDatabaseType();
    int targetColumnIndex = 1;
    int dataItemsCount = 0;

    for (int currentRow = 0; currentRow < numberOfRowsPerBatch; currentRow++)
    {
      final boolean ok = rs.next();

      if (!ok)
      {
        throw new MissingDataException("No more data in row " + currentRow + "/" + numberOfRowsPerBatch);
      }

      targetDatabaseConfiguration.beforeNewRow(targetConnection, targetConnectorId, targetTableMetaData);

      for (int columnIndex = 1; columnIndex <= sourceColumns.size(); columnIndex++)
      {
        final ColumnMetaData sourceColumnMetaData = sourceColumns.get(columnIndex - 1);
        final ColumnMapperResult mapping = columnMapper.map(sourceColumnMetaData, targetTableMetaData);

        if (mapping.getColumns().isEmpty())
        {
          if (mapping.isEmptyColumnListOk())
          {
            // Unused result, but we may have to skip the next data item from an underlying stream implementation
            rs.getObject(columnIndex);
          }
          else
          {
            throw new IncompatibleColumnsException("Cannot map column " + targetTableMetaData
                    + ":"
                    + sourceColumnMetaData
                    + ": Target column list empty");
          }
        }

        for (final ColumnMetaData targetColumnMetaData : mapping.getColumns())
        {
          final ColumnTypeMapping columnTypeMapping = findMapping(targetConnectorId, commonColumnTypeResolver,
                  sourceColumnMetaData, targetColumnMetaData);

          Object value = columnTypeMapping.getSourceColumnType().getValue(rs, columnIndex);
          value = columnTypeMapping.getColumnDataMapper().map(sourceColumnMetaData, targetColumnMetaData, value);
          Closeable optionalCloseableObject = columnTypeMapping.getTargetColumnType().setValue(insertStatement, targetColumnIndex++, value, targetDatabaseType,
                  targetColumnMetaData.getColumnType());

          if (optionalCloseableObject != null)
          {
            _closeableObjects.add(optionalCloseableObject);
          }

          dataItemsCount++;
        }
      }

      // Add another INSERT with one VALUES clause to BATCH
      if (!useMultipleValuesClauses)
      {
        insertStatement.addBatch();
        targetColumnIndex = 1;
      }

      targetDatabaseConfiguration.afterNewRow(targetConnection, targetConnectorId, targetTableMetaData);
    }

    // Add single INSERT with many VALUES clauses to BATCH
    if (useMultipleValuesClauses)
    {
      insertStatement.addBatch();
    }

    LOG.debug("Number of data items: " + dataItemsCount);
  }

  private ColumnTypeMapping findMapping(final String targetConnectorId,
                                        final CommonColumnTypeResolverTool commonColumnTypeResolver, final ColumnMetaData columnMetaData1,
                                        final ColumnMetaData columnMetaData2) throws SQLException
  {
    final ColumnTypeMapping columnTypeMapping = commonColumnTypeResolver.getCommonColumnTypeMapping(
      columnMetaData1, targetConnectorId, columnMetaData2);

    if (columnTypeMapping == null)
    {
      throw new IncompatibleColumnsException("Columns have incompatible types: " + columnMetaData1.getColumnName()
              + "/"
              + columnMetaData1.getColumnTypeName()
              + " vs. "
              + columnMetaData2.getColumnName()
              + "/"
              + columnMetaData2.getColumnTypeName());
    }

    return columnTypeMapping;
  }

  /**
   * Clear any resources associated with this commit, open BLOBs in particular.
   */
  public void clear()
  {
    for (Closeable closeableObject : _closeableObjects)
    {
      try
      {
        closeableObject.close();
      }
      catch (IOException e)
      {
        LOG.warn("While closing " + closeableObject, e);
      }
    }

    _closeableObjects.clear();
  }
}
