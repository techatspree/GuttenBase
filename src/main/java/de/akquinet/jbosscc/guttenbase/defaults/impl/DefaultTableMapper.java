package de.akquinet.jbosscc.guttenbase.defaults.impl;

import de.akquinet.jbosscc.guttenbase.hints.CaseConversionMode;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import java.sql.SQLException;

/**
 * By default prepend schema name.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DefaultTableMapper implements TableMapper {
  private final CaseConversionMode _caseConversionMode;

  public DefaultTableMapper(final CaseConversionMode caseConversionMode) {
    assert caseConversionMode != null : "caseConversionMode != null";
    _caseConversionMode = caseConversionMode;
  }

  public DefaultTableMapper() {
    this(CaseConversionMode.NONE);
  }

  @Override
  public TableMetaData map(final TableMetaData source, final DatabaseMetaData targetDatabaseMetaData) throws SQLException {
    final String tableName = mapTableName(source, targetDatabaseMetaData);
    return targetDatabaseMetaData.getTableMetaData(tableName);
  }

  @Override
  public String fullyQualifiedTableName(final TableMetaData source, final DatabaseMetaData targetDatabaseMetaData)
    throws SQLException {
    final String schemaPrefix = targetDatabaseMetaData.getSchemaPrefix();

    return schemaPrefix + mapTableName(source, targetDatabaseMetaData);
  }

  @Override
  public String mapTableName(final TableMetaData source, final DatabaseMetaData targetDatabaseMetaData) throws SQLException {
    return _caseConversionMode.convert(source.getTableName());
  }
}
