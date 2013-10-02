package de.akquinet.jbosscc.guttenbase.mapping;

import java.util.List;

/**
 * Determine list of used column type resolvers.
 * 
 * <p>&copy; 2012-2020 akquinet tech@spree</p>
 * 
 * @author M. Dahm
 */
public interface ColumnTypeResolverList {
  List<ColumnTypeResolver> getColumnTypeResolvers();
}