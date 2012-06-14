package de.akquinet.jbosscc.guttenbase.hints.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.akquinet.jbosscc.guttenbase.export.zip.ZipExporterClassResources;
import de.akquinet.jbosscc.guttenbase.export.zip.ZipStartup;
import de.akquinet.jbosscc.guttenbase.hints.ZipExporterClassResourcesHint;

/**
 * By default use the {@link ZipStartup} class as the main class of the JAR. Adds all GuttenBase and log4j classes to the JAR.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class DefaultZipExporterClassResourcesHint extends ZipExporterClassResourcesHint {
	@Override
	public ZipExporterClassResources getValue() {
		return new ZipExporterClassResources() {
			@Override
			public Class<?> getStartupClass() {
				return ZipStartup.class;
			}

			@Override
			public List<Class<?>> getClassResources() {
				final List<Class<?>> classes = new ArrayList<Class<?>>();
				classes.add(getStartupClass());
				classes.add(Logger.class);
				return classes;
			}
		};
	}
}
