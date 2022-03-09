package de.akquinet.jbosscc.guttenbase.hints;

/**
 * Used to map table names, column names, etc.
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public enum CaseConversionMode {
  NONE,
  UPPER,
  LOWER;

  public String convert(final String name) {
    assert name != null : "name != null";

    switch (this) {
      case LOWER:
        return name.toLowerCase();

      case UPPER:
        return name.toUpperCase();

      case NONE:
      default:
        return name;
    }
  }
}
