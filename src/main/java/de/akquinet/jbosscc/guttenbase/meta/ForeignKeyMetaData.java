package de.akquinet.jbosscc.guttenbase.meta;

import java.io.Serializable;
import java.util.List;

/**
 * Information about a foreign key between table columns.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public interface ForeignKeyMetaData extends Comparable<ForeignKeyMetaData>, Serializable {
    String getForeignKeyName();

    TableMetaData getTableMetaData();

    List<ColumnMetaData> getReferencingColumns();

    List<ColumnMetaData> getReferencedColumns();

    TableMetaData getReferencingTableMetaData();

    TableMetaData getReferencedTableMetaData();
}
