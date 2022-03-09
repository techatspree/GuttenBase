package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * Check if the given table is a "main" table in the sense that it represents an entity. In terms of JPA: the corresponding Java class is
 * annotated with @Entity.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public interface EntityTableChecker {
  /**
   * @return true if the given table is a "main" table in the sense that it represents an entity. In terms of JPA: the corresponding Java
   * class is annotated with @Entity.
   */
  boolean isEntityTable(TableMetaData tableMetaData);
}
