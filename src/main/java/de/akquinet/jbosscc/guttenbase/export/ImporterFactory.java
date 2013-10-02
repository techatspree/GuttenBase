package de.akquinet.jbosscc.guttenbase.export;


/**
 * Create @see {@link Importer} for reading dumped database using @see {@link ImportDumpConnector}.
 * 
 * <p>&copy; 2012-2020 akquinet tech@spree</p>
 * 
 * @author M. Dahm
 */
public interface ImporterFactory {
  Importer createImporter();
}
