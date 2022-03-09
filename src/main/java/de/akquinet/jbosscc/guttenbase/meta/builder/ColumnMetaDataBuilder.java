package de.akquinet.jbosscc.guttenbase.meta.builder;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.InternalColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.impl.ColumnMetaDataImpl;

import java.math.BigInteger;
import java.sql.Types;

/**
 * Builder class for ColumnMetaData.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ColumnMetaDataBuilder {
	private int _columnType = Types.BIGINT;
	private String _columnName = "ID";
	private String _columnTypeName = "BIGINT";
	private String _columnClassName = BigInteger.class.getName();
	private boolean _isNullable = true;
	private boolean _isAutoIncrement = false;
	private boolean _primaryKey = false;
	private int _precision = 0;
	private int _scale = 0;
	private final TableMetaDataBuilder _tableMetaDataBuilder;
	private ColumnMetaDataImpl _result;

	public ColumnMetaDataBuilder(final TableMetaDataBuilder tableMetaDataBuilder, final ColumnMetaData sourceColumnMetaData) {
		this(tableMetaDataBuilder);

		setAutoIncrement(sourceColumnMetaData.isAutoIncrement());
		setColumnClassName(sourceColumnMetaData.getColumnClassName());
		setColumnName(sourceColumnMetaData.getColumnName());
		setColumnType(sourceColumnMetaData.getColumnType());
		setNullable(sourceColumnMetaData.isNullable());
		setPrecision(sourceColumnMetaData.getPrecision());
		setPrimaryKey(sourceColumnMetaData.isPrimaryKey());
		setScale(sourceColumnMetaData.getScale());
	}

	public ColumnMetaDataBuilder(final TableMetaDataBuilder tableMetaDataBuilder) {
		_tableMetaDataBuilder = tableMetaDataBuilder;
	}

	public InternalColumnMetaData build() {
		if (_result == null) {
			_result = new ColumnMetaDataImpl(_columnType, _columnName, _columnTypeName, _columnClassName, _isNullable, _isAutoIncrement,
					_precision, _scale, _tableMetaDataBuilder.build());
			_result.setPrimaryKey(_primaryKey);
		}

		return _result;
	}

	public ColumnMetaDataBuilder setPrimaryKey(final boolean primaryKey) {
		_primaryKey = primaryKey;
		return this;
	}

	public ColumnMetaDataBuilder setColumnType(final int columnType) {
		_columnType = columnType;
		return this;
	}

	public String getColumnName() {
		return _columnName;
	}

	public ColumnMetaDataBuilder setColumnName(final String columnName) {
		_columnName = columnName;
		return this;
	}

	public ColumnMetaDataBuilder setColumnTypeName(final String columnTypeName) {
		_columnTypeName = columnTypeName;
		return this;
	}

	public ColumnMetaDataBuilder setColumnClassName(final String columnClassName) {
		_columnClassName = columnClassName;
		return this;
	}

	public ColumnMetaDataBuilder setNullable(final boolean isNullable) {
		_isNullable = isNullable;
		return this;
	}

	public ColumnMetaDataBuilder setAutoIncrement(final boolean isAutoIncrement) {
		_isAutoIncrement = isAutoIncrement;
		return this;
	}

	public ColumnMetaDataBuilder setPrecision(final int precision) {
		_precision = precision;
		return this;
	}

	public ColumnMetaDataBuilder setScale(final int scale) {
		_scale = scale;
		return this;
	}
}
