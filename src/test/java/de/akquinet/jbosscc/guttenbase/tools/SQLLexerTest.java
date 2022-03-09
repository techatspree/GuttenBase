package de.akquinet.jbosscc.guttenbase.tools;

import de.akquinet.jbosscc.guttenbase.sql.SQLLexer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SQLLexerTest {
  @Test
  public void testCreateStatement() throws Exception {
    final SQLLexer objectUnderTest = new SQLLexer(Arrays.asList("CREATE TABLE FOO_USER_COMPANY(", "USER_ID bigint,",
        " ASSIGNED_COMPANY_ID bigint,", "CONSTRAINT PK_FOO_USER_COMPANY PRIMARY KEY (USER_ID, ASSIGNED_COMPANY_ID));"));

    final List<String> list = objectUnderTest.parse();
    assertEquals(1, list.size());
    assertEquals(
        "CREATE TABLE FOO_USER_COMPANY( USER_ID bigint, ASSIGNED_COMPANY_ID bigint, CONSTRAINT PK_FOO_USER_COMPANY PRIMARY KEY (USER_ID, ASSIGNED_COMPANY_ID))",
        list.get(0));
  }

  @Test
  public void testDelimiter() throws Exception {
    final SQLLexer objectUnderTest = new SQLLexer(Arrays.asList("CREATE TABLE FOO(", "USER_ID bigint", ")@", "CREATE TABLE BAR(",
        "USER_ID bigint", ")@"), '@');

    final List<String> list = objectUnderTest.parse();
    assertEquals(2, list.size());
    assertEquals("CREATE TABLE FOO( USER_ID bigint )", list.get(0));
  }
}
