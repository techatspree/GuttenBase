package de.akquinet.jbosscc.guttenbase.hints;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.mapping.TableNameMapper;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

public class TestTableNameMapper implements TableNameMapper {
	@Override
	public String mapTableName(final TableMetaData tableMetaData) throws SQLException {
		final String tableName = tableMetaData.getTableName();

		return "\"" + tableName + "\"";
	}
}
