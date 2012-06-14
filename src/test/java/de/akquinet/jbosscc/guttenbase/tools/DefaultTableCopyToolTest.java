package de.akquinet.jbosscc.guttenbase.tools;

public class DefaultTableCopyToolTest extends AbstractTableCopyToolTest {
  @Override
  protected AbstractTableCopyTool getCopyTool() {
    return new DefaultTableCopyTool(_connectorRepository);
  }
}
