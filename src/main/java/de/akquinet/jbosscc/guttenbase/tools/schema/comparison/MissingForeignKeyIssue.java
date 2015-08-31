package de.akquinet.jbosscc.guttenbase.tools.schema.comparison;

import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;

public class MissingForeignKeyIssue extends SchemaCompatibilityIssue {
  private final ForeignKeyMetaData _foreignKeyMetaData;

  public MissingForeignKeyIssue(final String message, final ForeignKeyMetaData foreignKeyMetaData) {
    super(message);
    _foreignKeyMetaData = foreignKeyMetaData;
  }

  public ForeignKeyMetaData getForeignKeyMetaData() {
    return _foreignKeyMetaData;
  }

  @Override
  public SchemaCompatibilityIssueType getCompatibilityIssueType() {
    return SchemaCompatibilityIssueType.MISSING_FOREIGN_KEY;
  }
}
