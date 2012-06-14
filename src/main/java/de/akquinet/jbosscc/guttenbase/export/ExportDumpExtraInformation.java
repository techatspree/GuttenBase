package de.akquinet.jbosscc.guttenbase.export;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * Give the user a possibility to add extra informations to the dumped data.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface ExportDumpExtraInformation {
	Map<String, Serializable> getExtraInformation(ConnectorRepository connectorRepository, String connectorId,
			ExportDumpConnectorInfo exportDumpConnectionInfo) throws SQLException;
}
