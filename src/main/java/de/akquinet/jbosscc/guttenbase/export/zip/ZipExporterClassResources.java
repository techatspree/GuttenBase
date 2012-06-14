package de.akquinet.jbosscc.guttenbase.export.zip;

import java.util.List;

/**
 * When exporting to e JAR/ZIP file we allow to add custom classes and resources to the resultiung JAR.
 * 
 * This allows to create a self-contained executable JAR that will startup with a Main class customizable by the framework user.
 * 
 * <p>&copy; 2012 akquinet tech@spree</p>
 * 
 * @author M. Dahm
 */
public interface ZipExporterClassResources {
  /**
   * Startup class that will be written into the MANIFEST file.
   */
  Class<?> getStartupClass();

  /**
   * List of classes that need to be added to the JAR. I.e. all resources found on the same originating resource (whether from file system
   * or JAR) will be added to the JAR, too. The list should contain the startup class as the first entry.
   */
  List<Class<?>> getClassResources();
}
