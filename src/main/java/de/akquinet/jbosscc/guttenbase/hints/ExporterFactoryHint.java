package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.export.ExportDumpConnector;
import de.akquinet.jbosscc.guttenbase.export.Exporter;
import de.akquinet.jbosscc.guttenbase.export.ExporterFactory;

/**
 * Create @see {@link Exporter} for dumping database using @see {@link ExportDumpConnector}.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 *
 * Hint is used by {@link ExportDumpConnector} to determine exporter implementation
 * @author M. Dahm
 */
public abstract class ExporterFactoryHint implements ConnectorHint<ExporterFactory> {
	@Override
	public Class<ExporterFactory> getConnectorHintType() {
		return ExporterFactory.class;
	}
}
