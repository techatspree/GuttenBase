package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.repository.DatabaseColumnFilter;
import de.akquinet.jbosscc.guttenbase.repository.impl.DatabaseMetaDataInspectorTool;
import java.sql.DatabaseMetaData;

/**
 * Regard which columns when @see {@link DatabaseMetaDataInspectorTool} is inquiring the database for columns?
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 *
 * Hint is used by {@link DatabaseMetaDataInspectorTool} when reading tables from {@link DatabaseMetaData}
 * @author M. Dahm
 */
public abstract class DatabaseColumnFilterHint implements ConnectorHint<DatabaseColumnFilter>
{
  @Override
  public final Class<DatabaseColumnFilter> getConnectorHintType()
  {
    return DatabaseColumnFilter.class;
  }
}
