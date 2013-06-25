package de.akquinet.jbosscc.guttenbase.mapping;

import java.sql.SQLException;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Select target column(s) for given source column. Usually, this will a 1:1 relationship. However, there may be situations where
 * you want to duplicate or transform data into multiple columns. You also may want to drop columns from the source database.
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface ColumnMapper
{
  /**
   * Return matching columns in target table. Must not be NULL.
   */
  ColumnMapperResult map(ColumnMetaData source, TableMetaData targetTableMetaData) throws SQLException;

  public static class ColumnMapperResult
  {
    private final List<ColumnMetaData> _columns;
    private final boolean _emptyColumnListOk;

    public ColumnMapperResult(final List<ColumnMetaData> columns)
    {
      this(columns, false);
    }

    public ColumnMapperResult(final List<ColumnMetaData> columns, final boolean emptyColumnListOk)
    {
      assert columns != null : "columns != null";
      _columns = columns;
      _emptyColumnListOk = emptyColumnListOk;
    }

    public List<ColumnMetaData> getColumns()
    {
      return _columns;
    }

    /**
     * If the column cannot be found in the target table this raises an error. However, if you explicitely want to drop that
     * column and this method returns true it will just output a warning instead.
     */
    public boolean isEmptyColumnListOk()
    {
      return _emptyColumnListOk;
    }
  }
}
