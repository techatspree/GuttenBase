package de.akquinet.jbosscc.guttenbase.sql;

/**
 * Tokens for {@link SQLLexer}.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public enum SQLTokenType {
  WHITESPACE, SINGLE_LINE_COMMENT_START, MULTI_LINE_COMMENT_START, MULTI_LINE_COMMENT_END, STRING_DELIMITER_START, STRING_DELIMITER_END, END_OF_STATEMENT, END_OF_LINE, OTHER, EOF, ESCAPED_STRING_DELIMITER
}
