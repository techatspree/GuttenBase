package de.akquinet.jbosscc.guttenbase.tools.schema.comparison;

import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;

public class DuplicateIndexIssue extends IndexIssue {
  public DuplicateIndexIssue(final String message, final IndexMetaData indexMetaData) {
    super(message, SchemaCompatibilityIssueType.DUPLICATE_INDEX, indexMetaData);
  }
}
