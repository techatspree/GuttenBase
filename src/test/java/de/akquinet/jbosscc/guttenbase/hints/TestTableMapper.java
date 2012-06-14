package de.akquinet.jbosscc.guttenbase.hints;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

public class TestTableMapper implements TableMapper {
	@Override
	public TableMetaData map(final TableMetaData source, final DatabaseMetaData targetDatabaseMetaData) throws SQLException {
		final String tableName = source.getTableName().toUpperCase().replaceAll("Ö", "O").replace("Ä", "A").replaceAll("Ü", "U");

		return targetDatabaseMetaData.getTableMetaData(tableName);
	}
}
