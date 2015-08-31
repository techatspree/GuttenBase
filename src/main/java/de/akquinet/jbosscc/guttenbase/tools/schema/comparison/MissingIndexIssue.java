package de.akquinet.jbosscc.guttenbase.tools.schema.comparison;

import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;

public class MissingIndexIssue extends SchemaCompatibilityIssue {
  private final IndexMetaData _indexMetaData;

  public MissingIndexIssue(final String message, final IndexMetaData indexMetaData) {
    super(message);
    _indexMetaData = indexMetaData;
  }

  public IndexMetaData getIndexMetaData() {
    return _indexMetaData;
  }

  @Override
  public SchemaCompatibilityIssueType getCompatibilityIssueType() {
    return SchemaCompatibilityIssueType.MISSING_INDEX;
  }
}
