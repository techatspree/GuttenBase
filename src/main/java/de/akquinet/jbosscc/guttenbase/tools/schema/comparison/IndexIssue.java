package de.akquinet.jbosscc.guttenbase.tools.schema.comparison;

import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;

public abstract class IndexIssue extends SchemaCompatibilityIssue {
  private final SchemaCompatibilityIssueType _type;
  private final IndexMetaData _indexMetaData;

  public IndexIssue(final String message, final SchemaCompatibilityIssueType type, final IndexMetaData indexMetaData) {
    super(message);
    assert type != null : "type != null";
    assert indexMetaData != null : "indexMetaData != null";

    _type = type;
    _indexMetaData = indexMetaData;
  }

  public IndexMetaData getIndexMetaData() {
    return _indexMetaData;
  }

  @Override
  public SchemaCompatibilityIssueType getCompatibilityIssueType() {
    return _type;
  }
}
