package de.akquinet.jbosscc.guttenbase.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;

public class SwingProgressIndicator implements ProgressIndicator
{
  private final ProgressIndicatorPanel _panel = new ProgressIndicatorPanel();
  private final JDialog _dialog = new JDialog();
  private final TimingProgressIndicator _timingDelegate = new TimingProgressIndicator();
  private final StringBuilder _text = new StringBuilder();

  public SwingProgressIndicator()
  {
    _dialog.setModal(true);
    _dialog.setTitle("Copying tables...");
    _dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    final Dimension size = new Dimension(500, 300);
    _dialog.getContentPane().setLayout(new BorderLayout());
    _dialog.getContentPane().add(_panel, BorderLayout.CENTER);

    _dialog.setSize(size);
    _panel.setPreferredSize(size);
  }

  @Override
  public void initializeIndicator()
  {
    _timingDelegate.initializeIndicator();
    _panel.getTotalTime().setText("");
    _panel.getTableTime().setText("");
    _panel.getSourceTable().setText("");
    _panel.getTargetTable().setText("");

    final Thread thread = new Thread()
    {
      @Override
      public void run()
      {
        _dialog.setVisible(true);
      }
    };

    thread.setDaemon(true);
    thread.start();
  }

  @Override
  public void startCopying(final int numberOfTables)
  {
    _timingDelegate.startCopying(numberOfTables);

    _panel.getTotalProgress().setValue(0);
    _panel.getTotalProgress().setMinimum(0);
    _panel.getTotalProgress().setMaximum(numberOfTables);
  }

  @Override
  public void startCopyTable(final String sourceTableName, final int rowCount, final String targetTableName,
      final int numberOfRowsPerBatch)
  {
    _timingDelegate.startCopyTable(sourceTableName, rowCount, targetTableName, numberOfRowsPerBatch);

    _panel.getTableProgress().setMinimum(0);
    _panel.getTableProgress().setMaximum(rowCount);
    _panel.getTableProgress().setValue(0);
    _panel.getSourceTable().setText(sourceTableName);
    _panel.getTargetTable().setText(targetTableName);
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

    _panel.getTableProgress().setValue(totalCopiedRows);
  }

  @Override
  public void endCopyTable()
  {
    _timingDelegate.endCopyTable();

    _panel.getTableProgress().setValue(_timingDelegate.getRowCount());
    _panel.getTotalProgress().setValue(_timingDelegate.getTableCounter());
  }

  @Override
  public void warn(final String text)
  {
    _timingDelegate.warn(text);
    _text.append("WARNING: " + text + "\n");
    updateMessages();
  }

  @Override
  public void info(final String text)
  {
    _timingDelegate.info(text);
    _text.append("Info: " + text + "\n");
    updateMessages();
  }

  @Override
  public void debug(final String text)
  {
    _timingDelegate.debug(text);
    _text.append("Debug: " + text + "\n");
    updateMessages();
  }

  @Override
  public void finalizeIndicator()
  {
    _timingDelegate.finalizeIndicator();
    _dialog.setVisible(false);
    _dialog.dispose();
  }

  private void updateMessages()
  {
    _panel.getMessages().setText(_text.toString());
  }
}
