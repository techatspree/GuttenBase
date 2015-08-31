package de.akquinet.jbosscc.guttenbase.tools.schema.comparison;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

public class IncompatibleColumnsIssue extends SchemaCompatibilityIssue {
  private final ColumnMetaData _sourceColumn;
  private final ColumnMetaData _targetColumn;

  public IncompatibleColumnsIssue(final String message, final ColumnMetaData sourceColumn, final ColumnMetaData targetColumn) {
    super(message);
    _sourceColumn = sourceColumn;
    _targetColumn = targetColumn;
  }

  public ColumnMetaData getSourceColumn() {
    return _sourceColumn;
  }

  public ColumnMetaData getTargetColumn() {
    return _targetColumn;
  }

  @Override
  public SchemaCompatibilityIssueType getCompatibilityIssueType() {
    return SchemaCompatibilityIssueType.INCOMPATIBLE_COLUMNS;
  }
}
