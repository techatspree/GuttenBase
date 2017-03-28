package de.akquinet.jbosscc.guttenbase.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * Primitive implementation of SQL parser in order to check validity of script.
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class SQLLexer
{
  private static final int EOF = -1;

  private final String _sql;
  private int _currentIndex = 0;

  private boolean _withinString;
  private final char _delimiter;

  public SQLLexer(final List<String> lines)
  {
    this(lines, ';');
  }

  public SQLLexer(final List<String> lines, final char delimiter)
  {
    assert lines != null : "lines != null";

    _delimiter = delimiter;
    final StringBuilder builder = new StringBuilder();

    for (final String line : lines)
    {
      builder.append(line.trim());

      builder.append('\n');
    }

    _sql = builder.toString();
  }

  public List<String> parse()
  {
    final List<String> result = new ArrayList<>();
    final StringBuilder builder = new StringBuilder();

    while (hasNext())
    {
      final SQLTokenType nextToken = nextToken();

      switch (nextToken)
      {
      case END_OF_LINE:
      case WHITESPACE:
        read();
        builder.append(' ');

        while (nextToken() == SQLTokenType.WHITESPACE)
        {
          read();
        }

        break;

      case END_OF_STATEMENT:
        read();
        result.add(builder.toString().trim());
        builder.setLength(0);
        break;

      case SINGLE_LINE_COMMENT_START:
        seekToken(SQLTokenType.END_OF_LINE);
        break;

      case MULTI_LINE_COMMENT_START:
        seekToken(SQLTokenType.MULTI_LINE_COMMENT_END);
        break;

      case MULTI_LINE_COMMENT_END:
        read();
        read();
        break;

      case ESCAPED_STRING_DELIMITER:
        builder.append((char) read());
        builder.append((char) read());
        break;

      case EOF:
        read();
        break;

      case OTHER:
        builder.append((char) read());
        break;

      case STRING_DELIMITER_START:
        builder.append((char) read());
        _withinString = true;
        break;

      case STRING_DELIMITER_END:
        builder.append((char) read());
        _withinString = false;
        break;

      default:
        throw new IllegalStateException("unhandled case: " + nextToken);
      }
    }

    return result;
  }

  private void seekToken(final SQLTokenType tokenType)
  {
    SQLTokenType nextToken;

    do
    {
      read();
      nextToken = nextToken();
    }
    while (nextToken != tokenType && nextToken != SQLTokenType.EOF);
  }

  private SQLTokenType nextToken()
  {
    final int ch1 = read();
    final int ch2 = read();
    unread(2);

    if (ch1 < 0)
    {
      return SQLTokenType.EOF;
    }
    else if (!_withinString && ch1 == '-' && ch2 == '-')
    {
      return SQLTokenType.SINGLE_LINE_COMMENT_START;
    }
    else if (!_withinString && ch1 == '/' && ch2 == '*')
    {
      return SQLTokenType.MULTI_LINE_COMMENT_START;
    }
    else if (!_withinString && ch1 == '*' && ch2 == '/')
    {
      return SQLTokenType.MULTI_LINE_COMMENT_END;
    }
    else if (!_withinString && ch1 == _delimiter)
    {
      return SQLTokenType.END_OF_STATEMENT;
    }
    else if (!_withinString && ch1 == '\n')
    {
      return SQLTokenType.END_OF_LINE;
    }
    else if (!_withinString && ch1 == '\r' || ch1 == '\t' || ch1 == ' ')
    {
      return SQLTokenType.WHITESPACE;
    }
    else if (ch1 == '\'' && ch2 == '\'')
    {
      return SQLTokenType.ESCAPED_STRING_DELIMITER;
    }
    else if (ch1 == '\'' && ch2 != '\'')
    {
      if (_withinString)
      {
        return SQLTokenType.STRING_DELIMITER_END;
      }
      else
      {
        return SQLTokenType.STRING_DELIMITER_START;
      }
    }
    else
    {
      return SQLTokenType.OTHER;
    }
  }

  private void unread(final int count)
  {
    _currentIndex -= count;
  }

  private boolean hasNext()
  {
    return _currentIndex < _sql.length();
  }

  private int read()
  {
    final int index = _currentIndex++;

    if (!hasNext())
    {
      return EOF;
    }
    else
    {
      return _sql.charAt(index);
    }
  }
}
