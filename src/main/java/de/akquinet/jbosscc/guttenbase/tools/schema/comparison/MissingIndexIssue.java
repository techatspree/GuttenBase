package de.akquinet.jbosscc.guttenbase.tools.schema.comparison;

import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;

public class MissingIndexIssue extends IndexIssue {
  public MissingIndexIssue(final String message, final IndexMetaData indexMetaData) {
    super(message, SchemaCompatibilityIssueType.MISSING_INDEX, indexMetaData);
  }
}
