package de.akquinet.jbosscc.guttenbase.export.zip;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.meta.IndexMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * Default tool to start when "executing" the JAR file. It simply displays the file structure.
 * <p></p>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public class ZipStartup extends JPanel {
  private static final long serialVersionUID = 1L;

  public ZipStartup() {
    super(new BorderLayout());
  }

  public void initGUI() throws Exception {
    add(createButtonPanel(), BorderLayout.SOUTH);

    final DefaultTreeModel treeModel = createTreeModel();

    add(new JScrollPane(new JTree(treeModel)), BorderLayout.CENTER);
  }

  private DefaultTreeModel createTreeModel() throws Exception {
    final DatabaseMetaData databaseMetaData = readDatabaseMetaData();
    final DefaultMutableTreeNode rootNode = addRootNode(databaseMetaData);

    addTableNodes(databaseMetaData, rootNode);

    return new DefaultTreeModel(rootNode);
  }

  private DatabaseMetaData readDatabaseMetaData() throws IOException, ClassNotFoundException {
    final InputStream inputStream = ZipExporter.class.getResourceAsStream(ZipConstants.PATH_SEPARATOR + ZipConstants.META_DATA);
    final ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
    final DatabaseMetaData databaseMetaData = (DatabaseMetaData) objectInputStream.readObject();
    objectInputStream.close();
    return databaseMetaData;
  }

  private DefaultMutableTreeNode addRootNode(final DatabaseMetaData databaseMetaData) throws IOException, SQLException {
    final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(ZipConstants.GUTTEN_BASE_NAME);

    final Properties databaseMetaDataProperties = new ZipDatabaseMetaDataWriter().writeDatabaseMetaDataEntry(databaseMetaData)
      .getProperties();
    addMetaDataProperties(databaseMetaDataProperties, rootNode, ZipDatabaseMetaDataWriter.TABLE_NAME);
    return rootNode;
  }

  private void addTableNodes(final DatabaseMetaData databaseMetaData, final DefaultMutableTreeNode rootNode) throws IOException {
    final List<TableMetaData> tableMetaDatas = databaseMetaData.getTableMetaData();
    for (final TableMetaData tableMetaData : tableMetaDatas) {
      final Properties tableMetaDataProperties = new ZipTableMetaDataWriter().writeTableMetaDataEntry(tableMetaData).getProperties();

      final DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode(tableMetaData.getTableName());

      addMetaDataProperties(tableMetaDataProperties, tableNode, "XX");
      rootNode.add(tableNode);

      addColumnNodes(tableMetaData, tableNode);
      addIndexNodes(tableMetaData, tableNode);
    }
  }

  private void addColumnNodes(final TableMetaData tableMetaData, final DefaultMutableTreeNode tableNode) throws IOException {
    for (final ColumnMetaData columnMetaData : tableMetaData.getColumnMetaData()) {
      final Properties columnMetaDataProperties = new ZipColumnMetaDataWriter().writeColumnMetaDataEntry(columnMetaData).getProperties();
      final DefaultMutableTreeNode columnNode = new DefaultMutableTreeNode(columnMetaData.getColumnName());

      addMetaDataProperties(columnMetaDataProperties, columnNode, "XX");
      tableNode.add(columnNode);
    }
  }

  private void addIndexNodes(final TableMetaData tableMetaData, final DefaultMutableTreeNode tableNode) throws IOException {
    final DefaultMutableTreeNode indexesNode = new DefaultMutableTreeNode("Indexes");
    tableNode.add(indexesNode);

    for (final IndexMetaData indexMetaData : tableMetaData.getIndexes()) {
      final Properties columnMetaDataProperties = new ZipIndexMetaDataWriter().writeIndexMetaDataEntry(indexMetaData).getProperties();
      final DefaultMutableTreeNode indexNode = new DefaultMutableTreeNode(indexMetaData.getIndexName());

      addMetaDataProperties(columnMetaDataProperties, indexNode, "XX");
      indexesNode.add(indexNode);
    }
  }

  private void addMetaDataProperties(final Properties metaDataProperties, final DefaultMutableTreeNode rootNode,
                                     final String excludedProperty) throws IOException {
    for (@SuppressWarnings("rawtypes")
         final Enumeration keysEnum = metaDataProperties.keys(); keysEnum.hasMoreElements(); ) {
      final String key = keysEnum.nextElement().toString();
      final String value = metaDataProperties.getProperty(key);

      if (!key.startsWith(excludedProperty)) {
        rootNode.add(new DefaultMutableTreeNode(key + ": " + value));
      }
    }
  }

  private JPanel createButtonPanel() {
    final JPanel buttonPanel = new JPanel(new FlowLayout());

    final JButton close = new JButton("Close");
    close.addActionListener(e -> System.exit(0));
    buttonPanel.add(close);
    return buttonPanel;
  }

  public static void main(final String[] args) {
    try {
      final JFrame frame = new JFrame("GuttenBase GUI");
      final ZipStartup startup = new ZipStartup();
      frame.setContentPane(startup);
      startup.initGUI();

      final Dimension size = new Dimension(1200, 800);
      frame.setMinimumSize(size);
      frame.setSize(size);

      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
    } catch (final Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
  }
}
