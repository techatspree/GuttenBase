package de.akquinet.jbosscc.guttenbase.hints.impl;

import java.io.Serializable;
import java.util.Map;

import de.akquinet.jbosscc.guttenbase.export.ImportDumpExtraInformation;
import de.akquinet.jbosscc.guttenbase.hints.ImportDumpExtraInformationHint;

/**
 * By default do nothing.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultImportDumpExtraInformationHint extends ImportDumpExtraInformationHint {
	@Override
	public ImportDumpExtraInformation getValue() {
		return new ImportDumpExtraInformation() {
			@Override
			public void processExtraInformation(final Map<String, Serializable> extraInformation) throws Exception {
			}
		};
	}
}
