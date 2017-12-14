package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.export.ImportDumpConnector;
import de.akquinet.jbosscc.guttenbase.export.Importer;
import de.akquinet.jbosscc.guttenbase.export.ImporterFactory;

/**
 * Create @see {@link Importer} for reading dumped database using @see {@link ImportDumpConnector}.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 *
 * Hint is used by {@link ImportDumpConnector} to determine importer implementation
 * @author M. Dahm
 */
public abstract class ImporterFactoryHint implements ConnectorHint<ImporterFactory> {
	@Override
	public Class<ImporterFactory> getConnectorHintType() {
		return ImporterFactory.class;
	}
}
