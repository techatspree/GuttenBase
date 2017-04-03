package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.hints.RepositoryColumnFilterHint;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryColumnFilter;

/**
 * By default accept any column
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultRepositoryColumnFilterHint extends RepositoryColumnFilterHint {
	@Override
	public RepositoryColumnFilter getValue() {
		return column -> true;
	}
}
