package de.akquinet.jbosscc.guttenbase.export.zip;

import de.akquinet.jbosscc.guttenbase.utils.ResourceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Copy all classes and data that can be found relative to the given class resource to the generated JAR/ZIP.
 *
 * This allows us to create a self-contained executable JAR with a user defined startup class.
 *
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ZipClassesFromClassResourceExporter extends ZipResourceExporter
{
  public ZipClassesFromClassResourceExporter(final ZipOutputStream zipOutputStream)
  {
    super(zipOutputStream);
  }

  /**
   * Copy all classes and data that can be found relative to the given class resource to the generated JAR/ZIP.
   *
   * We support classes read from file system or JAR.
   */
  public void copyClassesToZip(final Class<?> startupClass) throws IOException
  {
    final ResourceUtil.ResourceInfo resourceInfo = new ResourceUtil().getResourceInfo(startupClass);

    if (resourceInfo.isJarFile())
    {
      copyClassesFromJar(resourceInfo.getJarFileOrFolder());
    }
    else
    {
      copyClassesFromFilesystem(resourceInfo.getJarFileOrFolder(), resourceInfo.getJarFileOrFolder().getPath());
    }
  }

  private void copyClassesFromFilesystem(final File dir, String rootPath) throws IOException
  {
    for (final File file : dir.listFiles())
    {
      addFileToJar(file, rootPath);
    }
  }

  private void addFileToJar(final File path, String rootPath) throws IOException
  {
    if (!path.isFile())
    {
      copyClassesFromFilesystem(path, rootPath);
    }
    else
    {
      final String name = path.getPath().substring(rootPath.length() + 1);
      final InputStream inputStream = new FileInputStream(path);

      addEntry(name, inputStream);
    }
  }

  private void copyClassesFromJar(final File path) throws IOException
  {
    final ZipFile zipFile = new ZipFile(path);

    for (final Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements(); )
    {
      final ZipEntry zipEntry = entries.nextElement();
      final InputStream inputStream = zipFile.getInputStream(zipEntry);

      addEntry(zipEntry.getName(), inputStream);
    }

    zipFile.close();
  }
}
