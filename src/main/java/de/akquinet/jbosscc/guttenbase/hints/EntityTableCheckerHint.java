package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.tools.AbstractSequenceUpdateTool;
import de.akquinet.jbosscc.guttenbase.tools.EntityTableChecker;

/**
 * Check if the given table is a "main" table in the sense that it represents an entity. In terms of JPA: the corresponding Java class is
 * annotated with @Entity.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 *
 *
 * Hint is used by {@link AbstractSequenceUpdateTool} to look for entity classes, i.e. classes that may use an ID sequence
 * @author M. Dahm
 */
public abstract class EntityTableCheckerHint implements ConnectorHint<EntityTableChecker> {
	@Override
	public final Class<EntityTableChecker> getConnectorHintType() {
		return EntityTableChecker.class;
	}
}
