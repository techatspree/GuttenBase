package de.akquinet.jbosscc.guttenbase.export.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import de.akquinet.jbosscc.guttenbase.utils.Util;

/**
 * Helper class to add resources to ZIP file.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class ZipResourceExporter {
  protected static final Logger LOG = Logger.getLogger(ZipResourceExporter.class);

  private final ZipOutputStream _zipOutputStream;
  private final Set<String> _entries = new HashSet<>();

  public ZipResourceExporter(final ZipOutputStream zipOutputStream) {
    assert zipOutputStream != null : "zipOutputStream != null";
    _zipOutputStream = zipOutputStream;
  }

  public void addEntry(String name, final InputStream inputStream) throws IOException {
    assert name != null : "name != null";
    assert inputStream != null : "inputStream != null";

    // Escape problems with DOS/Windows in ZIP entries
    name = name.replace('\\', ZipConstants.PATH_SEPARATOR);

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
