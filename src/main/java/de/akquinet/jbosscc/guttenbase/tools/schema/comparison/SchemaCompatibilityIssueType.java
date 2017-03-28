package de.akquinet.jbosscc.guttenbase.tools.schema.comparison;

import java.util.logging.Level;

/**
 * Type of compatibility issue
 *
 * @copyright akquinet tech@spree GmbH, 2002-2040
 */
public enum SchemaCompatibilityIssueType {
  MISSING_TABLE(Level.SEVERE),
  ADDITIONAL_TABLE(Level.INFO),
  MISSING_COLUMN(Level.SEVERE),
  INCOMPATIBLE_COLUMNS(Level.SEVERE),
  DROPPED_COLUMN(Level.WARNING),
  ADDITIONAL_NONNULL_COLUMN(Level.SEVERE),
  ADDITIONAL_COLUMN(Level.WARNING),
  MISSING_INDEX(Level.INFO),
  MISSING_FOREIGN_KEY(Level.WARNING);

  private final Level _severity;

  SchemaCompatibilityIssueType(final Level severity) {

    _severity = severity;
  }

  public Level getSeverity() {
    return _severity;
  }
}
