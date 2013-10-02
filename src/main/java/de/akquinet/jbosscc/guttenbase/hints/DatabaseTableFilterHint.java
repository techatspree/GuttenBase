package de.akquinet.jbosscc.guttenbase.hints;

import java.sql.DatabaseMetaData;

import de.akquinet.jbosscc.guttenbase.connector.impl.AbstractConnector;
import de.akquinet.jbosscc.guttenbase.repository.DatabaseTableFilter;
import de.akquinet.jbosscc.guttenbase.repository.impl.DatabaseMetaDataInspectorTool;

/**
 * Regard which tables when {@link DatabaseMetaDataInspectorTool} is looking for tables in the given data base. The
 * {@link DatabaseMetaDataInspectorTool} is triggered by default in {@link AbstractConnector#retrieveDatabaseMetaData()}.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @Applicable-For-Source
 * @Hint-Used-By {@link DatabaseMetaDataInspectorTool} when reading tables from {@link DatabaseMetaData}
 * @author M. Dahm
 */
public abstract class DatabaseTableFilterHint implements ConnectorHint<DatabaseTableFilter> {
	@Override
	public final Class<DatabaseTableFilter> getConnectorHintType() {
		return DatabaseTableFilter.class;
	}
}
