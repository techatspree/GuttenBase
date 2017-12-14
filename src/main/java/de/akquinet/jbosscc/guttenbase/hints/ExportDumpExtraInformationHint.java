package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.export.ExportDumpExtraInformation;
import de.akquinet.jbosscc.guttenbase.export.plain.PlainGzipExporter;
import de.akquinet.jbosscc.guttenbase.export.zip.ZipExporter;

/**
 * When exporting to e JAR/ZIP file we give the user a possibility to add extra informations to the dumped data.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 *
 * Hint is used by {@link ZipExporter} to add custom informations to the dump
 * Hint is used by {@link PlainGzipExporter} to add custom informations to the dump
 * @author M. Dahm
 */
public abstract class ExportDumpExtraInformationHint implements ConnectorHint<ExportDumpExtraInformation> {
	@Override
	public Class<ExportDumpExtraInformation> getConnectorHintType() {
		return ExportDumpExtraInformation.class;
	}
}
