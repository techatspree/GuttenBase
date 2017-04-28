package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultTableMapper;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import java.sql.SQLException;

public class TestTableMapper extends DefaultTableMapper {
  @Override
  public TableMetaData map(final TableMetaData source, final DatabaseMetaData targetDatabaseMetaData) throws SQLException {
    final String tableName = source.getTableName().toUpperCase().replaceAll("Ö", "O").replace("Ä", "A").replaceAll("Ü", "U");

    return targetDatabaseMetaData.getTableMetaData(tableName);
  }
}
