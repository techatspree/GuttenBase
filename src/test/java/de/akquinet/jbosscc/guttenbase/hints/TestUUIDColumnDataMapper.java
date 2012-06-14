package de.akquinet.jbosscc.guttenbase.hints;

import java.sql.SQLException;
import java.util.UUID;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnDataMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

/**
 * Convert long ID to UUID
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class TestUUIDColumnDataMapper implements ColumnDataMapper {
	@Override
	public Object map(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData, final Object value) {
		final Number number = (Number) value;

		if (number != null) {
			final long id = number.longValue();
			final ColumnMetaData referencedColumn = sourceColumnMetaData.getReferencedColumn();
			String uuid;

			if (referencedColumn != null) {
				uuid = createKey(referencedColumn, id);
			} else {
				uuid = createKey(sourceColumnMetaData, id);
			}

			return uuid;
		} else {
			return null;
		}
	}

	@Override
	public boolean isApplicable(final ColumnMetaData sourceColumnMetaData, final ColumnMetaData targetColumnMetaData) throws SQLException {
		final String sourceColumnName = sourceColumnMetaData.getColumnName().toUpperCase();
		final String targetColumnName = targetColumnMetaData.getColumnName().toUpperCase();

		return sourceColumnName.equals(targetColumnName) && sourceColumnName.endsWith("ID");
	}

	/**
	 * Very simple way to create UUID. We create it from the column's hash code and the old id. You might want to use something mor
	 * complicated.
	 */
	private String createKey(final ColumnMetaData columnMetaData, final long id) {
		final String key = columnMetaData.getTableMetaData().getTableName() + ":" + columnMetaData.getColumnName();

		final UUID uuid = new UUID(key.hashCode(), id);

		return uuid.toString();
	}
}
