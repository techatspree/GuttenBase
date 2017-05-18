package de.akquinet.jbosscc.guttenbase.tools.schema.comparison;

import de.akquinet.jbosscc.guttenbase.meta.ForeignKeyMetaData;

public abstract class ForeignKeyIssue extends SchemaCompatibilityIssue {
  private final SchemaCompatibilityIssueType _type;
  private final ForeignKeyMetaData _foreignKeyMetaData;

  public ForeignKeyIssue(final String message, final SchemaCompatibilityIssueType type, final ForeignKeyMetaData foreignKeyMetaData) {
    super(message);

    assert type != null : "type != null";
    assert foreignKeyMetaData != null : "foreignKeyMetaData != null";

    _type = type;
    _foreignKeyMetaData = foreignKeyMetaData;
  }

  public ForeignKeyMetaData getForeignKeyMetaData() {
    return _foreignKeyMetaData;
  }

  @Override
  public SchemaCompatibilityIssueType getCompatibilityIssueType() {
    return _type;
  }
}
