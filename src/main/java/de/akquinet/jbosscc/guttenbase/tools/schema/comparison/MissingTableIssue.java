package de.akquinet.jbosscc.guttenbase.tools.schema.comparison;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

public class MissingTableIssue extends SchemaCompatibilityIssue {
  private final TableMetaData _sourceTableMetaData;

  public MissingTableIssue(final String message, final TableMetaData sourceTableMetaData) {
    super(message);

    _sourceTableMetaData = sourceTableMetaData;
  }

  public TableMetaData getSourceTableMetaData() {
    return _sourceTableMetaData;
  }

  @Override
  public SchemaCompatibilityIssueType getCompatibilityIssueType() {
    return SchemaCompatibilityIssueType.MISSING_TABLE;
  }
}
