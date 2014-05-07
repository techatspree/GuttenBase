package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultColumnMapper;
import de.akquinet.jbosscc.guttenbase.hints.ColumnMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;

/**
 * By default return column with same name ignoring case.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultColumnMapperHint extends ColumnMapperHint {
	@Override
	public ColumnMapper getValue() {
		return new DefaultColumnMapper();
	}
}
