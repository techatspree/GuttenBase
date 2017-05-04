package de.akquinet.jbosscc.guttenbase.tools.copyTablesFromH2ToDerby;

import de.akquinet.jbosscc.guttenbase.tools.AbstractTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;

public class DefaultAllShopTableCopyToolTest extends AbstractAllShopTableCopyToolTest {
  @Override
  protected AbstractTableCopyTool getCopyTool() {
    return new DefaultTableCopyTool(_connectorRepository);
  }
}
