package de.akquinet.jbosscc.guttenbase.export.zip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.ZipOutputStream;

/**
 * Base implementation the gathers all properties and stores them into a file of the ZIP.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public abstract class ZipAbstractMetaDataWriter {
  private final Properties _properties = new Properties() {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Enumeration keys() {
      final Enumeration keysEnum = super.keys();
      final Vector keyList = new Vector();

      while (keysEnum.hasMoreElements()) {
        keyList.add(keysEnum.nextElement().toString());
      }

      Collections.sort(keyList);
      return keyList.elements();
    }
  };

  protected void setProperty(final String key, final String value) {
    _properties.setProperty(key, value);
  }

  public Properties getProperties() {
    return _properties;
  }

  /**
   * Store gathered properties as text in current ZIP file entry.
   */
  public void store(final String comment, final ZipOutputStream zipOutputStream) throws IOException {
    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    final PrintStream printStream = new PrintStream(bos);
    _properties.store(printStream, comment);
    printStream.close();

    zipOutputStream.write(bos.toByteArray());
  }
}
