package de.akquinet.jbosscc.guttenbase.repository.impl;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeResolver;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;

/**
 * Try to resolve by JDBC class name.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ClassNameColumnTypeResolver implements ColumnTypeResolver {
  /**
   * Try to resolve by JDBC class name.
   */
  @Override
  public ColumnType getColumnType(final ColumnMetaData columnMetaData) throws SQLException {
    final String columnClassName = columnMetaData.getColumnClassName();

    return ColumnType.isSupportedClass(columnClassName) ? ColumnType.valueForClass(columnClassName) : ColumnType.CLASS_UNKNOWN;
  }
}
