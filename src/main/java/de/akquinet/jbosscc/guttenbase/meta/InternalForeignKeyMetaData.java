package de.akquinet.jbosscc.guttenbase.meta;

/**
 * Extension for internal access.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public interface InternalForeignKeyMetaData extends ForeignKeyMetaData {
    void addColumnTuple(final ColumnMetaData referencingColumn, final ColumnMetaData referencedColumn);
}