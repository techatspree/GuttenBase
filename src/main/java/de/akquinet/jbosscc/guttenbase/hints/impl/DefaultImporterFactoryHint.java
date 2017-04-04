package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.export.ImporterFactory;
import de.akquinet.jbosscc.guttenbase.export.zip.ZipImporter;
import de.akquinet.jbosscc.guttenbase.hints.ImporterFactoryHint;

/**
 * Default implementation uses {@link ZipImporter}.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultImporterFactoryHint extends ImporterFactoryHint {
	@Override
	public ImporterFactory getValue() {
		return ZipImporter::new;
	}
}
