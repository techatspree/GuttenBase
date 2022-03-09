package de.akquinet.jbosscc.guttenbase.sql;

/**
 * Tokens for {@link SQLLexer}.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class SQLToken {
  private final SQLTokenType _sqlTokenType;
  private final String _token;

  public SQLToken(final SQLTokenType sqlTokenType, final String token) {
    _sqlTokenType = sqlTokenType;
    _token = token;
  }

  public SQLTokenType getSqlTokenType() {
    return _sqlTokenType;
  }

  public String getToken() {
    return _token;
  }

  @Override
  public String toString() {
    return getSqlTokenType().name();
  }
}
