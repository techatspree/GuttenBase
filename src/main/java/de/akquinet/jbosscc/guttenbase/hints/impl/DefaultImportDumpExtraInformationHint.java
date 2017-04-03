package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.export.ImportDumpExtraInformation;
import de.akquinet.jbosscc.guttenbase.hints.ImportDumpExtraInformationHint;

/**
 * By default do nothing.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultImportDumpExtraInformationHint extends ImportDumpExtraInformationHint {
	@Override
	public ImportDumpExtraInformation getValue() {
		return extraInformation -> {
        };
	}
}
