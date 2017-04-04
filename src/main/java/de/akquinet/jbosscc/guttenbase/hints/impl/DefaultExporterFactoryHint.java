package de.akquinet.jbosscc.guttenbase.hints.impl;

import de.akquinet.jbosscc.guttenbase.export.ExporterFactory;
import de.akquinet.jbosscc.guttenbase.export.zip.ZipExporter;
import de.akquinet.jbosscc.guttenbase.hints.ExporterFactoryHint;

/**
 * Default implementation uses {@link ZipExporter}.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultExporterFactoryHint extends ExporterFactoryHint {
	@Override
	public ExporterFactory getValue() {
		return ZipExporter::new;
	}
}
