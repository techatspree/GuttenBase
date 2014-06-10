package de.akquinet.jbosscc.guttenbase.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

public class ResourceUtil
{
  public ResourceInfo getResourceInfo(Class<?> clazz) throws IOException
  {
    final String pathToClass = '/' + clazz.getName().replace('.', '/') + ".class";
    final URL resource = clazz.getResource(pathToClass);

    String path = resource.getPath();
    final String protocol = resource.getProtocol();

    // file:/Users/mdahm/projects/workspace/GuttenBase/target/test-classes/de/akquinet/jbosscc/guttenbase/utils/ResourceUtilTest.class
    if ("file".equalsIgnoreCase(protocol))
    {
      path = resource.getPath().substring(0, path.length() - pathToClass.length());
    }
    // jar:file:/Data/maven/repository/junit/junit-dep/4.8.1/junit-dep-4.8.1.jar!/org/junit/Test.class
    else if ("jar".equalsIgnoreCase(protocol))
    {
      path = resource.getPath().substring(0, path.length() - (pathToClass.length() + 1));

      if (path.startsWith("file:"))
      {
        path = path.substring(5);
      }
    }
    else
    {
      throw new IOException("Cannot handle protocol " + protocol + " while reading classes");
    }

    if (Util.isWindows() && path.startsWith("/"))
    {
      path = path.substring(1);
    }

    path = URLDecoder.decode(path, "UTF-8");

    return new ResourceInfo(protocol, new File(path), pathToClass);
  }

  public static class ResourceInfo
  {
    private final String _protocol;
    private final File _jarFileOrFolder;
    private final String _pathToClass;

    private ResourceInfo(final String protocol, final File jarFileOrFolder, final String pathToClass)
    {
      _protocol = protocol;
      _jarFileOrFolder = jarFileOrFolder;
      _pathToClass = pathToClass;
    }

    public String getProtocol()
    {
      return _protocol;
    }

    public File getJarFileOrFolder()
    {
      return _jarFileOrFolder;
    }

    public String getPathToClass()
    {
      return _pathToClass;
    }

    public boolean isJarFile()
    {
      return _jarFileOrFolder.canRead() && _jarFileOrFolder.isFile();
    }
  }
}
