package de.akquinet.jbosscc.guttenbase.meta.builder;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.meta.InternalDatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.impl.DatabaseMetaDataImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder class for DatabaseMetaData.
 * <p></p>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DatabaseMetaDataBuilder {
  private final List<TableMetaDataBuilder> _tables = new ArrayList<>();
  private String _schema = "";
  private DatabaseType _databaseType = DatabaseType.GENERIC;
  private DatabaseMetaDataImpl _result;
  private final Map<String, Object> _databaseProperties = new HashMap<>();

  public InternalDatabaseMetaData build() {
    if (_result == null) {
      _result = new DatabaseMetaDataImpl(_schema, _databaseProperties, _databaseType);

      for (final TableMetaDataBuilder builder : _tables) {
        _result.addTableMetaData(builder.build());
      }
    }

    return _result;
  }

  public DatabaseMetaDataBuilder addDatabaseProperty(final String methodName, final Object value) {
    _databaseProperties.put(methodName, value);
    return this;
  }

  public DatabaseMetaDataBuilder setDatabaseType(final DatabaseType databaseType) {
    _databaseType = databaseType;
    return this;
  }

  public DatabaseMetaDataBuilder setSchema(final String schema) {
    _schema = schema;
    return this;
  }

  public DatabaseMetaDataBuilder addTable(final TableMetaDataBuilder tableMetaDataBuilder) {
    _tables.add(tableMetaDataBuilder);
    return this;
  }
}
