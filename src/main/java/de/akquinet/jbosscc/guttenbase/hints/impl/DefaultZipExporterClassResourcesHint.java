package de.akquinet.jbosscc.guttenbase.hints.impl;


import de.akquinet.jbosscc.guttenbase.defaults.impl.DefaultZipExporterClassResources;
import de.akquinet.jbosscc.guttenbase.export.zip.ZipExporterClassResources;
import de.akquinet.jbosscc.guttenbase.export.zip.ZipStartup;
import de.akquinet.jbosscc.guttenbase.hints.ZipExporterClassResourcesHint;

/**
 * By default use the {@link ZipStartup} class as the main class of the JAR. Adds all GuttenBase and log4j classes to the JAR.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class DefaultZipExporterClassResourcesHint extends ZipExporterClassResourcesHint {
  @Override
  public ZipExporterClassResources getValue() {
    return new DefaultZipExporterClassResources();
  }
}
