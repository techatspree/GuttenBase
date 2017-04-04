package de.akquinet.jbosscc.guttenbase.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

/**
 * Collection of utility methods.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public abstract class Util {
  private static final Logger LOG = Logger.getLogger(Util.class);
  public static final Class<?> ByteArrayClass = byte[].class;

  public static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

  /**
   * Read all non-empty lines for File and remove and trim them.
   * 
   * @param resourceName
   *          Text file in CLASSPATH
   * @return array of strings
   */
  public static List<String> readLinesFromFile(final String resourceName, final String encoding) {
    final InputStream stream = getResourceAsStream(resourceName);

    if (stream != null) {
      return readLinesFromStream(stream, encoding);
    } else {
      LOG.warn(resourceName + " not found");
    }

    return new ArrayList<>();
  }

  public static InputStream getResourceAsStream(final String resource) {
    final String stripped = resource.startsWith("/") ? resource.substring(1) : resource;
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL url =  getResourceFromClassloader(resource, stripped, classLoader);

    if (url == null) {
      url = getResourceFromClassloader(resource, stripped, Util.class.getClassLoader());
    }

    if (url == null) {
      LOG.debug("Trying getResource");

      url = Util.class.getResource(resource);

      if (url == null) {
        url = Util.class.getResource(stripped);
      }
    }

    if (url != null) {
      try {
        return url.openStream();
      } catch (final IOException e) {
        LOG.warn("Can't open stream on " + url);
      }
    }

    return null;
  }

  private static URL getResourceFromClassloader(final String resource, final String stripped, final ClassLoader classLoader) {
    LOG.debug("Trying class loader " + classLoader);

    URL url = null;

    if (classLoader instanceof URLClassLoader) {
      LOG.debug("Trying as UCL class loader");

      final URLClassLoader ucl = (URLClassLoader) classLoader;
      url = ucl.findResource(resource);

      if (url == null) {
        url = ucl.findResource(stripped);
      }
    }

    if (url == null) {
      url = classLoader.getResource(resource);
    }

    if (url == null) {
      url = classLoader.getResource(stripped);
    }

    return url;
  }

  /**
   * Read all non-empty lines and remove and trim them.
   * 
   * @param inputStream
   *          UTF8-encoded stream to read data from
   * @return list of strings
   */
  public static List<String> readLinesFromStream(final InputStream inputStream, final String encoding) {
    assert inputStream != null : "inputStream != null";
    final ArrayList<String> result = new ArrayList<>();

    try {
      final LineNumberReader reader = new LineNumberReader(new InputStreamReader(inputStream, encoding));

      String line;
      while ((line = reader.readLine()) != null) {
        line = trim(line);

        if (!"".equals(line)) {
          result.add(line);
        }
      }

      inputStream.close();
    } catch (final Exception e) {
      LOG.error("Reading from inputstream", e);
    }

    return result;
  }

  public static String trim(final String src) {
    return src == null ? "" : src.trim();
  }

  /**
   * Create deep copy of object.
   */
  public static <T> T copyObject(final Class<T> clazz, final T sourceObject) {
    try {
      final byte[] byteArray = toByteArray(sourceObject);
      return fromByteArray(clazz, byteArray);
    } catch (final Exception e) {
      throw new IllegalStateException("Can not copy ", e);
    }
  }

  /**
   * Serialize into byte array
   */
  public static byte[] toByteArray(final Object sourceObject) throws IOException {
    final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    final ObjectOutput out = new ObjectOutputStream(outStream);
    out.writeObject(sourceObject);
    out.flush();
    out.close();

    return outStream.toByteArray();
  }

  /**
   * Deserialize from byte array
   * 
   * @throws Exception
   */
  @SuppressWarnings("JavaDoc")
  public static <T> T fromByteArray(final Class<T> clazz, final byte[] byteArray) throws Exception {
    final ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
    final ObjectInputStream objectInputStream = new ObjectInputStream(bis);

    final T result = clazz.cast(objectInputStream.readObject());
    objectInputStream.close();
    return result;
  }

  /**
   * Deserialize from input stream
   * 
   * @throws Exception
   */
  @SuppressWarnings("JavaDoc")
  public static <T> T fromInputStream(final Class<T> clazz, final InputStream inputStream) throws Exception {
    final ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

    return clazz.cast(objectInputStream.readObject());
  }

  public static void copy(final InputStream input, final OutputStream output) throws IOException {
    final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    int n;

    while ((n = input.read(buffer)) > 0) {
      output.write(buffer, 0, n);
    }
  }

  public static String formatTime(final long millis) {
    long seconds = millis / 1000;
    long minutes = seconds / 60;
    final long hours = minutes / 60;
    minutes = minutes % 60;
    seconds = seconds % 60;

    return fillup(hours) + ":" + fillup(minutes) + ":" + fillup(seconds);
  }

  public static void deleteDirectory(final File directory) {
    assert directory != null : "directory != null";

    if (directory.exists() && directory.isDirectory()) {
      final String[] files = directory.list();

      for (final String fileName : files) {
        final File file = new File(directory, fileName);

        deleteDirectory(file);
      }
    }

    directory.delete();
  }

  private static String fillup(final long time) {
    return time > 9 ? String.valueOf(time) : "0" + time;
  }

  public static String getStringFromStream(final InputStream inputStream) throws IOException {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    copy(inputStream, outputStream);
    final String result = outputStream.toString();
    inputStream.close();
    outputStream.close();
    return result;
  }

  public static boolean isWindows() {
    final String os = System.getProperty("os.name").toLowerCase();
    return os.contains("win");
  }

  /**
   * @return uppercased list of columns in SELECT statement
   */
  public static List<String> parseSelectedColumns(final String sql) throws SQLException {
    final List<String> result = new ArrayList<>();
    final StringTokenizer stringTokenizer = new StringTokenizer(sql, " ,\n\r\t");

    if (!"SELECT".equalsIgnoreCase(stringTokenizer.nextToken())) {
      throw new SQLException("Cannot parse statement: No SELECT clause " + sql);
    }

    for (String column = stringTokenizer.nextToken(); stringTokenizer.hasMoreTokens(); column = stringTokenizer.nextToken()) {
      if ("FROM".equalsIgnoreCase(column)) {
        return result;
      } else {
        result.add(column.toUpperCase());
      }
    }

    throw new SQLException("Cannot parse statement No FROM clause: " + sql);
  }

}
