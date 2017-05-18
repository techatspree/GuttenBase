package de.akquinet.jbosscc.guttenbase.tools.schema.comparison;

import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;

public class DuplicateForeignKeyIssue extends ForeignKeyIssue {
  public DuplicateForeignKeyIssue(final String message, final ForeignKeyMetaData foreignKeyMetaData) {
    super(message, SchemaCompatibilityIssueType.DUPLICATE_FOREIGN_KEY, foreignKeyMetaData);
  }
}
