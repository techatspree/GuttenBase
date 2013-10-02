package de.akquinet.jbosscc.guttenbase.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;

public class SwingProgressIndicator implements ProgressIndicator
{
  private final ProgressIndicatorPanel _panel = new ProgressIndicatorPanel();
  private final JDialog _dialog = new JDialog();

  public SwingProgressIndicator()
  {
    _dialog.setModal(true);
    _dialog.setTitle("Copying tables...");
    _dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    final Dimension size = new Dimension(300, 70);
    _dialog.getContentPane().setLayout(new BorderLayout());
    _dialog.getContentPane().add(_panel, BorderLayout.CENTER);

    _dialog.setSize(size);
    _panel.setPreferredSize(size);
  }

  @Override
  public void initializeIndicator()
  {
    _dialog.setVisible(true);
  }

  @Override
  public void startCopying(final int numberOfTables)
  {}

  @Override
  public void startCopyTable(final String sourceTableName, final int rowCount, final String targetTableName,
      final int numberOfRowsPerBatch)
  {}

  @Override
  public void startBatch()
  {}

  @Override
  public void endBatch(final int totalCopiedRows)
  {}

  @Override
  public void endCopyTable()
  {}

  @Override
  public void warn(final String string)
  {}

  @Override
  public void info(final String text)
  {}

  @Override
  public void debug(final String text)
  {}

  @Override
  public void finalizeIndicator()
  {
    _dialog.setVisible(false);
    _dialog.dispose();
  }

}
