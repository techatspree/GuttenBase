package de.akquinet.jbosscc.guttenbase.repository;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.repository.impl.DatabaseMetaDataInspectorTool;

import java.sql.SQLException;

/**
 * Regard which columns when @see {@link DatabaseMetaDataInspectorTool} is inquiring the database for columns?
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public interface DatabaseColumnFilter
{
  /**
   * Perform custom check on column before adding it to table meta data
   */
  boolean accept(ColumnMetaData columnMetaData) throws SQLException;
}
