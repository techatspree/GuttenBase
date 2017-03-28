package de.akquinet.jbosscc.guttenbase.defaults.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import de.akquinet.jbosscc.guttenbase.export.zip.ZipExporter;
import de.akquinet.jbosscc.guttenbase.export.zip.ZipExporterClassResources;
import de.akquinet.jbosscc.guttenbase.export.zip.ZipStartup;

/**
 * Default implementation.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultZipExporterClassResources implements ZipExporterClassResources {
  @Override
  public Class<?> getStartupClass() {
    return ZipStartup.class;
  }

  @Override
  public List<Class<?>> getClassResources() {
    final List<Class<?>> classes = new ArrayList<>();
    classes.add(getStartupClass());
    classes.add(ZipExporter.class); // Representing all GuttenBase classes
    classes.add(Logger.class);
    classes.add(IOUtils.class);
    return classes;
  }

  @Override
  public Map<String, URL> getUrlResources() {
    return new HashMap<>();
  }
}