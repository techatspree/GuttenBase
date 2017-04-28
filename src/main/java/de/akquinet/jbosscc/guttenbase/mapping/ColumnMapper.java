package de.akquinet.jbosscc.guttenbase.mapping;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * Select target column(s) for given source column. Usually, this will a 1:1 relationship. However, there may be situations where
 * you want to duplicate or transform data into multiple columns. You also may want to drop columns from the source database.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
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

  /**
   * Map the way column names are used in statements. Usually you won't need that, but sometimes you want to map the names, e.g. to add `name`
   * backticks, in order to escape special characters such as white space.
   */
  String mapColumnName(ColumnMetaData source, TableMetaData targetTableMetaData) throws SQLException;


  class ColumnMapperResult
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
     * If the column cannot be found in the target table this raises an error. However, if you explicitly want to drop that
     * column and this method returns true it will just output a warning instead.
     */
    public boolean isEmptyColumnListOk()
    {
      return _emptyColumnListOk;
    }
  }
}
