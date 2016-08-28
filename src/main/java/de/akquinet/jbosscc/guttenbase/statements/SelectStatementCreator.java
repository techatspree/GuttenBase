package de.akquinet.jbosscc.guttenbase.statements;

import java.sql.SQLException;
import java.sql.Types;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Create SELECT statement for copying data.
 * <p></p>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class SelectStatementCreator extends AbstractSelectStatementCreator {
  public SelectStatementCreator(final ConnectorRepository connectorRepository, final String connectorId) {
    super(connectorRepository, connectorId);
  }

  /**
   * Try to retrieve data in some deterministic order
   */
  @Override
  protected String createOrderBy(final TableMetaData tableMetaData) throws SQLException {
    final StringBuilder buf = new StringBuilder("ORDER BY ");
    int columnsAdded = 0;

		// No BLOB or the like for ordering
		final boolean isOracleOrMssql = DatabaseType.ORACLE.equals(tableMetaData.getDatabaseMetaData().getDatabaseType())
				|| DatabaseType.MSSQL.equals(tableMetaData.getDatabaseMetaData().getDatabaseType());
		final int rangeFrom = isOracleOrMssql ? Types.NULL : Types.LONGNVARCHAR; // Doesn't like LONG e.g.
		final int rangeTo = Types.JAVA_OBJECT;

    for (int i = 0; i < tableMetaData.getColumnCount(); i++) {
      final ColumnMetaData columnMetaData = tableMetaData.getColumnMetaData().get(i);
      final String columnName = _columnMapper.mapColumnName(columnMetaData, tableMetaData);
      final int jdbcType = columnMetaData.getColumnType();

      if (jdbcType > rangeFrom && jdbcType < rangeTo) {
        buf.append(columnName).append(", ");
        columnsAdded++;
      }
    }

    if (columnsAdded > 0) {
      buf.setLength(buf.length() - 2);

      return buf.toString();
    } else {
      return "";
    }
  }
}
