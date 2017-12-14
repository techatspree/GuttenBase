package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.export.zip.ZipExporter;
import de.akquinet.jbosscc.guttenbase.export.zip.ZipExporterClassResources;

/**
 * When exporting to e JAR/ZIP file we allow to add custom classes and resources to the resulting JAR.
 *
 * This allows to create a self-contained executable JAR that will startup with a Main class customizable by the framework user.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 *
 * Hint is used by {@link ZipExporter} to add custom classes to the generated JAR and configure the META-INF/MANIFEST.MF Main-Class entry
 * @author M. Dahm
 */
public abstract class ZipExporterClassResourcesHint implements ConnectorHint<ZipExporterClassResources> {
	@Override
	public Class<ZipExporterClassResources> getConnectorHintType() {
		return ZipExporterClassResources.class;
	}
}
