package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultTableMapper;
import de.akquinet.jbosscc.guttenbase.hints.TableMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;

/**
 * By default return table with same name ignoring case.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DefaultTableMapperHint extends TableMapperHint {
	@Override
	public TableMapper getValue() {
		return new DefaultTableMapper();
	}
}
