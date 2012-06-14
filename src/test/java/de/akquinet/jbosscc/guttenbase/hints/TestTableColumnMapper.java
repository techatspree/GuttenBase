package de.akquinet.jbosscc.guttenbase.hints;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultColumnMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

public final class TestTableColumnMapper extends DefaultColumnMapper {
	@Override
	public List<ColumnMetaData> map(final ColumnMetaData source, final TableMetaData targetTableMetaData) throws SQLException {
		final String columnName = source.getColumnName();

		if (columnName.equalsIgnoreCase("ID")) {
			final String newColumnName = mapColumnName(source);
			final ColumnMetaData columnMetaData = targetTableMetaData.getColumnMetaData(newColumnName);

			return columnMetaData != null ? new ArrayList<ColumnMetaData>(Arrays.asList(columnMetaData)) : null;
		} else {
			return super.map(source, targetTableMetaData);
		}
	}

	private String mapColumnName(final ColumnMetaData columnMetaData) throws SQLException {
		final String tableName = columnMetaData.getTableMetaData().getTableName().substring("FOO_".length());
		return tableName + "_ID";
	}
}