package de.akquinet.jbosscc.guttenbase.tools;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.sql.SQLLexer;

public class SQLLexerTest
{
  @Test
  public void testCreateStatement() throws Exception
  {
    final SQLLexer objectUnderTest = new SQLLexer(Arrays.asList("CREATE TABLE FOO_USER_COMPANY(", "USER_ID bigint,",
        " ASSIGNED_COMPANY_ID bigint,", "CONSTRAINT PK_FOO_USER_COMPANY PRIMARY KEY (USER_ID, ASSIGNED_COMPANY_ID));"));

    final List<String> list = objectUnderTest.parse();
    assertEquals(1, list.size());
    assertEquals(
        "CREATE TABLE FOO_USER_COMPANY( USER_ID bigint, ASSIGNED_COMPANY_ID bigint, CONSTRAINT PK_FOO_USER_COMPANY PRIMARY KEY (USER_ID, ASSIGNED_COMPANY_ID))",
        list.get(0));
  }
}
