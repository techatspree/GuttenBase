package de.akquinet.jbosscc.guttenbase.hints.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnDataMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnDataMapperProvider;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;

/**
 * Default implementation. To add further mapping you should simply extend {@link DefaultColumnDataMapperProviderHint} and call
 * {@link #addMapping(ColumnType, ColumnType, ColumnDataMapper)} in the overridden
 * {@link DefaultColumnDataMapperProviderHint#addMappings(DefaultColumnDataMapperProvider)} method.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultColumnDataMapperProvider implements ColumnDataMapperProvider {
	private final Map<String, ColumnDataMapper> _mappings = new HashMap<String, ColumnDataMapper>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnDataMapper findMapping(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData,
			final ColumnType sourceColumnType, final ColumnType targetColumnType) throws SQLException {
		final ColumnDataMapper columnDataMapper = findMapping(sourceColumnType, targetColumnType);

		if (columnDataMapper != null) {
			if (columnDataMapper.isApplicable(sourceColumnMetaData, targetColumnMetaData)) {
				return columnDataMapper;
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMapping(final ColumnType sourceColumnType, final ColumnType targetColumnType, final ColumnDataMapper columnDataMapper) {
		_mappings.put(createKey(sourceColumnType, targetColumnType), columnDataMapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeMapping(final ColumnType sourceColumnType, final ColumnType targetColumnType) {
		_mappings.remove(createKey(sourceColumnType, targetColumnType));
	}

	private String createKey(final ColumnType sourceColumnType, final ColumnType targetColumnType) {
		return sourceColumnType.name() + ":" + targetColumnType.name();
	}

	private ColumnDataMapper findMapping(final ColumnType sourceColumnType, final ColumnType targetColumnType) {
		return _mappings.get(createKey(sourceColumnType, targetColumnType));
	}
}
