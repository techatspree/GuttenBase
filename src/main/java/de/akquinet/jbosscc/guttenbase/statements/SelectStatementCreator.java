package de.akquinet.jbosscc.guttenbase.statements;

import java.sql.SQLException;
import java.sql.Types;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Create SELECT statement for copying data.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
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

		for (int i = 0; i < tableMetaData.getColumnCount(); i++) {
			final ColumnMetaData columnMetaData = tableMetaData.getColumnMetaData().get(i);
			final String columnName = _columnNameMapper.mapColumnName(columnMetaData);
			final int jdbcType = columnMetaData.getColumnType();

			// No BLOB or the like
			if (jdbcType < Types.JAVA_OBJECT) {
				buf.append(columnName + ", ");
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
