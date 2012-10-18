package de.akquinet.jbosscc.guttenbase.export.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import de.akquinet.jbosscc.guttenbase.utils.Util;

/**
 * Copy all classes and data that can be found relative to the given class
 * resource to the generated JAR/ZIP.
 * 
 * This allows us to create a self-contained executable JAR with a user defined
 * startup class.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ZipClassesFromClassResourceExporter {
	private static final Logger LOG = Logger.getLogger(ZipClassesFromClassResourceExporter.class);

	private final ZipOutputStream _zipOutputStream;
	private final Set<String> _entries = new HashSet<String>();

	public ZipClassesFromClassResourceExporter(final ZipOutputStream zipOutputStream) {
		_zipOutputStream = zipOutputStream;
	}

	/**
	 * Copy all classes and data that can be found relative to the given class
	 * resource to the generated JAR/ZIP.
	 * 
	 * We support classes read from file system or JAR.
	 */
	public void copyClassesToZip(final Class<?> startupClass) throws IOException {
		final String pathToClass = '/' + startupClass.getName().replace('.', '/') + ".class";
		final URL resource = startupClass.getResource(pathToClass);

		String path = resource.getPath();
		final String protocol = resource.getProtocol();
		boolean jarFile;

		if ("file".equalsIgnoreCase(protocol)) {
			path = resource.getPath().substring(0, path.length() - pathToClass.length());
			jarFile = false;
		} else if ("jar".equalsIgnoreCase(protocol)) {
			path = resource.getPath().substring(0, path.length() - (pathToClass.length() + 1));
			jarFile = true;

			if (path.startsWith("file:")) {
				path = path.substring(5);
			}
		} else {
			throw new IOException("Cannot handle protocol " + protocol + " while reading classes");
		}

		if (Util.isWindows() && path.startsWith("/")) {
			path = path.substring(1);
		}

		path = URLDecoder.decode(path, "UTF-8");

		if (jarFile) {
			copyClassesFromJar(path);
		} else {
			copyClassesFromFilesystem(path);
		}
	}

	private void copyClassesFromFilesystem(final String path) throws IOException {
		final File dir = new File(path);

		addDirectoryToJar(dir, path);
	}

	private void addDirectoryToJar(final File dir, final String path) throws IOException {
		assert path != null : "path!= null";
		assert dir != null : "dir!= null";

		for (final File file : dir.listFiles()) {
			addFileToJar(file, path);
		}
	}

	private void addFileToJar(final File file, final String path) throws IOException, FileNotFoundException {
		if (!file.isFile()) {
			addDirectoryToJar(file, path);
		} else {
			final String name = file.getPath().substring(path.length() + 1);
			final InputStream inputStream = new FileInputStream(file);

			addEntry(name, inputStream);
		}
	}

	private void copyClassesFromJar(final String path) throws FileNotFoundException, IOException {
		final ZipFile zipFile = new ZipFile(new File(path));

		for (final Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements();) {
			final ZipEntry zipEntry = entries.nextElement();
			final InputStream inputStream = zipFile.getInputStream(zipEntry);

			addEntry(zipEntry.getName(), inputStream);
		}

		zipFile.close();
	}

	private void addEntry(final String name, final InputStream inputStream) throws IOException {
		if (!_entries.add(name.toUpperCase()) || name.equalsIgnoreCase(ZipConstants.MANIFEST_NAME)) {
			LOG.warn("Duplicate entry ignored: " + name);
		} else {
			final ZipEntry zipEntry = new ZipEntry(name);
			_zipOutputStream.putNextEntry(zipEntry);
			Util.copy(inputStream, _zipOutputStream);
			inputStream.close();
			_zipOutputStream.closeEntry();
		}
	}
}
