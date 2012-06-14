package de.akquinet.jbosscc.guttenbase.export;

/**
 * Denote start of new table in export file
 * 
 * <p>&copy; 2012 akquinet tech@spree</p>
 * 
 * @author M. Dahm
 */
public class ExportTableHeaderImpl implements ExportTableHeader {
  private static final long serialVersionUID = 1L;

  private final String _tableName;

  public ExportTableHeaderImpl(final String tableName) {
    _tableName = tableName;
  }

  @Override
  public String getTableName() {
    return _tableName;
  }

  @Override
  public String toString() {
    return getTableName();
  }
}
