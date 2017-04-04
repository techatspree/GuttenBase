package de.akquinet.jbosscc.guttenbase.tools.schema.comparison;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SchemaCompatibilityIssues {
  private final List<SchemaCompatibilityIssue> _compatibilityIssues = new ArrayList<>();

  public void addIssue(final SchemaCompatibilityIssue issue) {
    _compatibilityIssues.add(issue);
  }

  public List<SchemaCompatibilityIssue> getCompatibilityIssues() {
    return new ArrayList<>(_compatibilityIssues);
  }

  public boolean isSevere() {
    for (final SchemaCompatibilityIssue compatibilityIssue : _compatibilityIssues) {
      if (Level.SEVERE.equals(compatibilityIssue.getCompatibilityIssueType().getSeverity())) {
        return true;
      }
    }

    return false;
  }

  public SchemaCompatibilityIssue contains(final SchemaCompatibilityIssueType issueType) {
    for (final SchemaCompatibilityIssue compatibilityIssue : _compatibilityIssues) {
      if (issueType == compatibilityIssue.getCompatibilityIssueType()) {
        return compatibilityIssue;
      }
    }

    return null;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();

    for (final SchemaCompatibilityIssue compatibilityIssue : _compatibilityIssues) {
      builder.append(compatibilityIssue).append("\n");
    }

    return builder.toString();
  }
}
