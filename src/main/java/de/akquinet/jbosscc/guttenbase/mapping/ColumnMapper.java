package de.akquinet.jbosscc.guttenbase.mapping;

import java.sql.SQLException;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Select target column(s) for given source column. Usually, this will a 1:1 relationship. However, there may be situations where you want
 * to duplicate or transform data into multiple columns.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface ColumnMapper {
	/**
	 * Return according columns in target table. Must not be NULL.
	 */
	List<ColumnMetaData> map(ColumnMetaData source, TableMetaData targetTableMetaData) throws SQLException;
}
