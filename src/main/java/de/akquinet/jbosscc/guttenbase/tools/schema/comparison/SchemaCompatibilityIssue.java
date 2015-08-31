package de.akquinet.jbosscc.guttenbase.tools.schema.comparison;

public abstract class SchemaCompatibilityIssue {
  private final String _message;

  public SchemaCompatibilityIssue(final String message) {
    assert message != null : "message != null";

    _message = message;
  }

  public abstract SchemaCompatibilityIssueType getCompatibilityIssueType();

  public String getMessage() {
    return _message;
  }

  @Override
  public String toString() {
    return getCompatibilityIssueType() + ":" + _message;
  }
}
