package de.akquinet.jbosscc.guttenbase.hints.impl;

import java.util.HashMap;

import de.akquinet.jbosscc.guttenbase.export.ExportDumpExtraInformation;
import de.akquinet.jbosscc.guttenbase.hints.ExportDumpExtraInformationHint;

/**
 * By default do nothing.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultExportDumpExtraInformationHint extends ExportDumpExtraInformationHint {
	@Override
	public ExportDumpExtraInformation getValue() {
		return (connectorRepository, connectorId, exportDumpConnectionInfo) -> new HashMap<>();
	}
}
