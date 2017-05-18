package de.akquinet.jbosscc.guttenbase.tools.schema.comparison;

import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;

public class MissingForeignKeyIssue extends ForeignKeyIssue {
  public MissingForeignKeyIssue(final String message, final ForeignKeyMetaData foreignKeyMetaData) {
    super(message, SchemaCompatibilityIssueType.MISSING_FOREIGN_KEY, foreignKeyMetaData);
  }
}
