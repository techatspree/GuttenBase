package de.akquinet.jbosscc.guttenbase.mapping;

import java.sql.SQLException;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

/**
 * Map the way column names are used in statements. Usually you won't need that, but sometimes you want to map the names, e.g. to add `name`
 * backticks, in order to escape special characters such as white space.
 * 
 * (C) 2012 by akquinet tech@spree
 * 
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public interface ColumnNameMapper {
	String mapColumnName(ColumnMetaData columnMetaData) throws SQLException;
}
