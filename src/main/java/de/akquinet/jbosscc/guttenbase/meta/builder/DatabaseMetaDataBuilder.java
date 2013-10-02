package de.akquinet.jbosscc.guttenbase.meta.builder;

import java.util.ArrayList;
import java.util.List;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.meta.InternalDatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.impl.DatabaseMetaDataImpl;

/**
 * Builder class for DatabaseMetaData.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DatabaseMetaDataBuilder {
	private final List<TableMetaDataBuilder> _tables = new ArrayList<TableMetaDataBuilder>();
	private String _schema = "";
	private String _databaseName = "SomeDB";
	private int _majorVersion = 1;
	private int _minorVersion = 0;
	private DatabaseType _databaseType = DatabaseType.GENERIC;
	private DatabaseMetaDataImpl _result;

	public InternalDatabaseMetaData build() {
		if (_result == null) {
			_result = new DatabaseMetaDataImpl(_schema, _databaseName, _majorVersion, _minorVersion, _databaseType);

			for (final TableMetaDataBuilder builder : _tables) {
				_result.addTableMetaData(builder.build());
			}
		}

		return _result;
	}

	public DatabaseMetaDataBuilder setDatabaseType(final DatabaseType databaseType) {
		_databaseType = databaseType;
		return this;
	}

	public DatabaseMetaDataBuilder setSchema(final String schema) {
		_schema = schema;
		return this;
	}

	public DatabaseMetaDataBuilder setDatabaseName(final String databaseName) {
		_databaseName = databaseName;
		return this;
	}

	public DatabaseMetaDataBuilder setMajorVersion(final int majorVersion) {
		_majorVersion = majorVersion;
		return this;
	}

	public DatabaseMetaDataBuilder setMinorVersion(final int minorVersion) {
		_minorVersion = minorVersion;
		return this;
	}

	public DatabaseMetaDataBuilder addTable(final TableMetaDataBuilder tableMetaDataBuilder) {
		_tables.add(tableMetaDataBuilder);
		return this;
	}
}
