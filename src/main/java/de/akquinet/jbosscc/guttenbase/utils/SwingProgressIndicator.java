package de.akquinet.jbosscc.guttenbase.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;

public class SwingProgressIndicator implements ProgressIndicator
{
  private final ProgressIndicatorPanel _panel = new ProgressIndicatorPanel();
  private final JDialog _dialog = new JDialog();
  private final TimingProgressIndicator _timingDelegate = new TimingProgressIndicator();

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
    _timingDelegate.initializeIndicator();
    _dialog.setVisible(true);
  }

  @Override
  public void startCopying(final int numberOfTables)
  {
    _timingDelegate.startCopying(numberOfTables);
  }

  @Override
  public void startCopyTable(final String sourceTableName, final int rowCount, final String targetTableName,
      final int numberOfRowsPerBatch)
  {
    _timingDelegate.startCopyTable(sourceTableName, rowCount, targetTableName, numberOfRowsPerBatch);
  }

  @Override
  public void startBatch()
  {
    _timingDelegate.startBatch();
  }

  @Override
  public void endBatch(final int totalCopiedRows)
  {
    _timingDelegate.endBatch(totalCopiedRows);

  }

  @Override
  public void endCopyTable()
  {
    _timingDelegate.endCopyTable();

  }

  @Override
  public void warn(final String text)
  {
    _timingDelegate.warn(text);
  }

  @Override
  public void info(final String text)
  {
    _timingDelegate.info(text);
  }

  @Override
  public void debug(final String text)
  {
    _timingDelegate.debug(text);
  }

  @Override
  public void finalizeIndicator()
  {
    _timingDelegate.finalizeIndicator();
    _dialog.setVisible(false);
    _dialog.dispose();
  }

}
