package de.akquinet.jbosscc.guttenbase.export;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;

/**
 * Give the user a possibility to add extra informations to the dumped data.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public interface ExportDumpExtraInformation {
	Map<String, Serializable> getExtraInformation(ConnectorRepository connectorRepository, String connectorId,
			ExportDumpConnectorInfo exportDumpConnectionInfo) throws SQLException;
}
