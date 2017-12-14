package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultColumnDataMapper;
import de.akquinet.jbosscc.guttenbase.hints.ColumnDataMapperProviderHint;
import de.akquinet.jbosscc.guttenbase.hints.ColumnTypeResolverListHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnDataMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnDataMapperProvider;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapping;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeResolver;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeResolverList;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import java.sql.SQLException;
import java.util.List;

/**
 * Try to find common type mapping usable for both columns.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * Hint is used by {@link ColumnDataMapperProviderHint} to determine mapping between different column types
 * Hint is used by {@link ColumnTypeResolverListHint} to determine mapping strategies between different column types
 *
 * @author M. Dahm
 */
public class CommonColumnTypeResolverTool {
	private final ConnectorRepository _connectorRepository;

	public CommonColumnTypeResolverTool(final ConnectorRepository connectorRepository) {
		assert connectorRepository != null : "connectorRepository != null";
		_connectorRepository = connectorRepository;
	}

	/**
	 * Returns column type usable for both columns or null if none can be found.
	 */
	public ColumnTypeMapping getCommonColumnTypeMapping(final ColumnMetaData sourceColumnMetaData,
																											final String targetConnectorId, final ColumnMetaData targetColumnMetaData) throws SQLException {
		final List<ColumnTypeResolver> columnTypeResolvers = _connectorRepository
				.getConnectorHint(targetConnectorId, ColumnTypeResolverList.class).getValue().getColumnTypeResolvers();

		for (final ColumnTypeResolver columnTypeResolver : columnTypeResolvers) {
			final ColumnTypeMapping result = findMapping(columnTypeResolver, sourceColumnMetaData, targetColumnMetaData, targetConnectorId);

			if (result != null) {
				return result;
			}
		}

		return null;
	}

	public ColumnType getColumnType(final String connectorId, final ColumnMetaData columnMetaData) throws SQLException {
		final List<ColumnTypeResolver> columnTypeResolvers = _connectorRepository.getConnectorHint(connectorId, ColumnTypeResolverList.class)
				.getValue().getColumnTypeResolvers();

		for (final ColumnTypeResolver columnTypeResolver : columnTypeResolvers) {
			final ColumnType columnType = columnTypeResolver.getColumnType(columnMetaData);

			if (!ColumnType.CLASS_UNKNOWN.equals(columnType)) {
				return columnType;
			}
		}

		return ColumnType.CLASS_UNKNOWN;
	}

	private ColumnTypeMapping findMapping(final ColumnTypeResolver columnTypeResolver, final ColumnMetaData sourceColumnMetaData,
			final ColumnMetaData targetColumnMetaData, final String targetConnectorId) throws SQLException {
		final ColumnType sourceColumnType = columnTypeResolver.getColumnType(sourceColumnMetaData);
		final ColumnType targetColumnType = columnTypeResolver.getColumnType(targetColumnMetaData);

		if (!ColumnType.CLASS_UNKNOWN.equals(sourceColumnType) && !ColumnType.CLASS_UNKNOWN.equals(targetColumnType)) {
			final ColumnDataMapperProvider columnDataMapperFactory = _connectorRepository.getConnectorHint(targetConnectorId,
					ColumnDataMapperProvider.class).getValue();

			final ColumnDataMapper columnDataMapper = columnDataMapperFactory.findMapping(sourceColumnMetaData, targetColumnMetaData,
					sourceColumnType, targetColumnType);

			if (columnDataMapper != null) {
				return new ColumnTypeMapping(sourceColumnType, targetColumnType, columnDataMapper);
			} else if (sourceColumnType.equals(targetColumnType)) {
				return new ColumnTypeMapping(sourceColumnType, targetColumnType, new DefaultColumnDataMapper());
			}
		}

		return null;
	}
}
