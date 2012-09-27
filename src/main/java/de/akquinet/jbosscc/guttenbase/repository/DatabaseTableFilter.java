package de.akquinet.jbosscc.guttenbase.repository;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.impl.DatabaseMetaDataInspectorTool;

/**
 * Regard which tables when @see {@link DatabaseMetaDataInspectorTool} is looking for tables?
 * 
 * The methods refer to the parameters passed to JDBC data base meta data methods such as s
 * {@linkplain DatabaseMetaData#getTables(String, String, String, String[])}
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface DatabaseTableFilter {

  String getCatalog() throws SQLException;

  String getSchemaPattern() throws SQLException;

  String getTableNamePattern() throws SQLException;

  String[] getTableTypes() throws SQLException;

  /**
   * Additionally you may add checks to the resulting meta data object
   */
  boolean accept(TableMetaData table) throws SQLException;
}
