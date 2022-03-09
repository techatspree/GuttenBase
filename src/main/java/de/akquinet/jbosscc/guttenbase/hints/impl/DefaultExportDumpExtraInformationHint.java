package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.export.ExportDumpExtraInformation;
import de.akquinet.jbosscc.guttenbase.hints.ExportDumpExtraInformationHint;

import java.util.HashMap;

/**
 * By default do nothing.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
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
