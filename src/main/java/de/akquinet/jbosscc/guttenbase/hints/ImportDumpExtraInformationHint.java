package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.export.ImportDumpExtraInformation;
import de.akquinet.jbosscc.guttenbase.export.plain.PlainGzipImporter;
import de.akquinet.jbosscc.guttenbase.export.zip.ZipImporter;

/**
 * When exporting to JAR/ZIP file we give the user a possibility to retrieve extra informations from the dumped data.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @Applicable-For-Target
 * @Hint-Used-By {@link ZipImporter} to add custom informations to the dump
 * @Hint-Used-By {@link PlainGzipImporter} to add custom informations to the dump
 * @author M. Dahm
 */
public abstract class ImportDumpExtraInformationHint implements ConnectorHint<ImportDumpExtraInformation> {
	@Override
	public Class<ImportDumpExtraInformation> getConnectorHintType() {
		return ImportDumpExtraInformation.class;
	}
}
