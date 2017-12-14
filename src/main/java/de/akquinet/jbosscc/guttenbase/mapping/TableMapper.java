package de.akquinet.jbosscc.guttenbase.mapping;

import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import java.sql.SQLException;

/**
 * Select target table for given source table.
 * <p></p>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public interface TableMapper {
  /**
   * @return matching table in target data base or null
   */
  TableMetaData map(TableMetaData source, DatabaseMetaData targetDatabaseMetaData) throws SQLException;

  /**
   * @return plain table name in target data base
   */
  String mapTableName(TableMetaData source, DatabaseMetaData targetDatabaseMetaData) throws SQLException;

  /**
   * @return fully qualifed table name in target data base, i.e. including schema prefix and escape characters
   */
  String fullyQualifiedTableName(TableMetaData source, DatabaseMetaData targetDatabaseMetaData) throws SQLException;
}
