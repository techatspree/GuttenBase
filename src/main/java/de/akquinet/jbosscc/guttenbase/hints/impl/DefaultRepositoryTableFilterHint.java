package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.RepositoryTableFilterHint;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryTableFilter;

/**
 * Default implementation will accept any table.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultRepositoryTableFilterHint extends RepositoryTableFilterHint {
	@Override
	public RepositoryTableFilter getValue() {
		return table -> true;
	}
}
