package de.akquinet.jbosscc.guttenbase.mapping;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Map table names, e.g. prepend schema name schema.table or add back ticks (`) to escape special names.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public interface TableNameMapper {
  String mapTableName(TableMetaData tableMetaData) throws SQLException;
}
