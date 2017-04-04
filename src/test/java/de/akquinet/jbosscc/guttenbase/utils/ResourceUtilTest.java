package de.akquinet.jbosscc.guttenbase.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ResourceUtilTest
{
  private final ResourceUtil _objectUnderTest = new ResourceUtil();

  @Test
  public void testResource() throws IOException
  {
    final ResourceUtil.ResourceInfo resourceInfoJar = _objectUnderTest.getResourceInfo(Test.class);
    Assert.assertTrue(resourceInfoJar.isJarFile());
    Assert.assertTrue(resourceInfoJar.getJarFileOrFolder().getAbsolutePath().endsWith("junit-dep-4.8.1.jar"));
    Assert.assertTrue(resourceInfoJar.getPathToClass().endsWith("org/junit/Test.class"));

    final ResourceUtil.ResourceInfo resourceInfoFile = _objectUnderTest.getResourceInfo(ResourceUtilTest.class);
    Assert.assertFalse(resourceInfoFile.isJarFile());
    Assert.assertTrue(resourceInfoFile.getJarFileOrFolder().getAbsolutePath(), resourceInfoFile.getJarFileOrFolder().getAbsolutePath().endsWith("test-classes"));
    Assert.assertTrue(resourceInfoFile.getPathToClass().endsWith("ResourceUtilTest.class"));
    final File thisFile = new File(resourceInfoFile.getJarFileOrFolder(), resourceInfoFile.getPathToClass());
    Assert.assertTrue(thisFile.getAbsolutePath(), thisFile.exists());
  }
}
