package de.akquinet.jbosscc.guttenbase.utils;

import de.akquinet.jbosscc.guttenbase.connector.GuttenBaseException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class UtilTest {
  @Test
  public void testFormatTime() {
    assertEquals("00:00:00", Util.formatTime(0));
    assertEquals("00:11:25", Util.formatTime(685000));
    assertEquals("02:04:14", Util.formatTime(((2 * 60 * 60) + (4 * 60) + 14) * 1000));
  }

  @Test
  public void testParseSelect() {
    assertEquals(Arrays.asList("A", "B"), Util.parseSelectedColumns("select a,  b From bla"));
    assertEquals(Collections.singletonList("A"), Util.parseSelectedColumns("SELECT a FROM bla"));
  }

  @Test(expected = GuttenBaseException.class)
  public void testNoSelect() {
    Util.parseSelectedColumns("Hallo");
  }

  @Test(expected = GuttenBaseException.class)
  public void testNoFrom() {
    Util.parseSelectedColumns("SELECT a FOrM bla");
  }
}
