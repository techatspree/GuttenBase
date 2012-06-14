package de.akquinet.jbosscc.guttenbase.hints.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import de.akquinet.jbosscc.guttenbase.export.ExportDumpConnectorInfo;
import de.akquinet.jbosscc.guttenbase.export.ExportDumpExtraInformation;
import de.akquinet.jbosscc.guttenbase.hints.ExportDumpExtraInformationHint;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

/**
 * By default do nothing.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultExportDumpExtraInformationHint extends ExportDumpExtraInformationHint {
	@Override
	public ExportDumpExtraInformation getValue() {
		return new ExportDumpExtraInformation() {

			@Override
			public Map<String, Serializable> getExtraInformation(final ConnectorRepository connectorRepository, final String connectorId,
					final ExportDumpConnectorInfo exportDumpConnectionInfo) throws SQLException {
				return new HashMap<String, Serializable>();
			}
		};
	}
}
