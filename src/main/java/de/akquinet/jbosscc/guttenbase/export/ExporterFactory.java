package de.akquinet.jbosscc.guttenbase.export;


/**
 * Create @see {@link Exporter} for dumping database using @see {@link ExportDumpConnector}.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface ExporterFactory {
  Exporter createExporter();
}
