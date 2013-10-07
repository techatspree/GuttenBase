package de.akquinet.jbosscc.guttenbase.repository;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.impl.DatabaseMetaDataInspectorTool;

/**
 * Regard which columns when @see {@link DatabaseMetaDataInspectorTool} is inquiring the database for columns?
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface DatabaseColumnFilter
{
  /**
   * Perform custom check on column before adding it to table meta data
   */
  boolean accept(TableMetaData table, ColumnMetaData columnMetaData) throws SQLException;
}
