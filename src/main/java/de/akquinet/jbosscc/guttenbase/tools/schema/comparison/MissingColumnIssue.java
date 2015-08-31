package de.akquinet.jbosscc.guttenbase.tools.schema.comparison;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

public class MissingColumnIssue extends SchemaCompatibilityIssue {
  private final ColumnMetaData _sourceColumn;

  public MissingColumnIssue(final String message, final ColumnMetaData sourceColumn) {
    super(message);

    _sourceColumn = sourceColumn;
  }

  public ColumnMetaData getSourceColumn() {
    return _sourceColumn;
  }

  @Override
  public SchemaCompatibilityIssueType getCompatibilityIssueType() {
    return SchemaCompatibilityIssueType.MISSING_COLUMN;
  }
}
