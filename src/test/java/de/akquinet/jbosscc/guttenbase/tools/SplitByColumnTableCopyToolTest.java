package de.akquinet.jbosscc.guttenbase.tools;

public class SplitByColumnTableCopyToolTest extends AbstractTableCopyToolTest {
  @Override
  protected AbstractTableCopyTool getCopyTool() {
    return new SplitByRangeTableCopyTool(_connectorRepository);
  }
}
