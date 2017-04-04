package de.akquinet.jbosscc.guttenbase.mapping;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

/**
 * Map data contained in a column to some other type. I.e., the target column may have a different type and thus an INSERT needs conversion.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public interface ColumnDataMapper {
	/**
	 * Mapper can be used for the given columns?
	 */
	boolean isApplicable(ColumnMetaData sourceColumnMetaData, ColumnMetaData targetColumnMetaData) throws SQLException;

	/**
	 * Map object. Must be able to handle NULL values.
	 */
	Object map(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData, Object value) throws SQLException;
}
