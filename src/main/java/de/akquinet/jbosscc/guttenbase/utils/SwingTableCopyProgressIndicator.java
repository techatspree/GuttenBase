package de.akquinet.jbosscc.guttenbase.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

/**
 * Swing UI for table copy
 * <p>
 * &copy; 2013-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class SwingTableCopyProgressIndicator implements TableCopyProgressIndicator
{
  private final TableCopyProgressIndicatorPanel _panel = new TableCopyProgressIndicatorPanel();
  private final JDialog _dialog = new JDialog();
  private final TimingProgressIndicator _timingDelegate = new TimingProgressIndicator();
  private final StringBuilder _text = new StringBuilder();
  private TimerDaemonThread _timerDaemonThread;

  public SwingTableCopyProgressIndicator()
  {
    _dialog.setModal(true);
    _dialog.setTitle("Copying tables...");

    _dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    _dialog.addWindowListener(new WindowAdapter()
    {
      @Override
      public void windowClosed(final WindowEvent e)
      {
        if (_dialog.isVisible() && _timerDaemonThread != null && _timerDaemonThread.isActive())
        {
          finalizeIndicator();
        }
      }
    });

    final Dimension size = new Dimension(800, 400);
    _dialog.getContentPane().setLayout(new BorderLayout());
    _dialog.getContentPane().add(_panel, BorderLayout.CENTER);

    _dialog.setSize(size);
    _dialog.setMinimumSize(size);
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

    _timerDaemonThread = new TimerDaemonThread(_dialog, _timingDelegate, this);
    _timerDaemonThread.start();
  }

  @Override
  public void startProcess(final int numberOfTables)
  {
    _timingDelegate.startProcess(numberOfTables);

    _panel.getTotalProgress().setValue(0);
    _panel.getTotalProgress().setMinimum(0);
    _panel.getTotalProgress().setMaximum(numberOfTables);
  }

  @Override
  public void startCopyTable(final String sourceTableName, final int rowCount, final String targetTableName)
  {
    _timingDelegate.startCopyTable(sourceTableName, rowCount, targetTableName);

    _panel.getTableProgress().setMinimum(0);
    _panel.getTableProgress().setMaximum(rowCount);
    _panel.getTableProgress().setValue(0);
    _panel.getSourceTable().setText(sourceTableName);
    _panel.getTargetTable().setText(targetTableName);
  }

  @Override
  public void startExecution()
  {
    _timingDelegate.startExecution();
  }

  @Override
  public void endExecution(final int totalCopiedRows)
  {
    _timingDelegate.endExecution(totalCopiedRows);

    _panel.getTableProgress().setValue(totalCopiedRows);
    updateTimers();
  }

  @Override
  public void endProcess()
  {
    _timingDelegate.endProcess();

    _panel.getTableProgress().setValue(_timingDelegate.getRowCount());
    _panel.getTotalProgress().setValue(_timingDelegate.getItemCounter());
  }

  @Override
  public void warn(final String text)
  {
    _timingDelegate.warn(text);
    _text.append("WARNING: ").append(text).append("\n");
    updateMessages();
  }

  @Override
  public void info(final String text)
  {
    _timingDelegate.info(text);
    _text.append("Info: ").append(text).append("\n");
    updateMessages();
  }

  @Override
  public void debug(final String text)
  {
    _timingDelegate.debug(text);
    _text.append("Debug: ").append(text).append("\n");
    updateMessages();
  }

  @Override
  public void finalizeIndicator()
  {
    _timingDelegate.finalizeIndicator();
    _timerDaemonThread.setActive(false);
    _dialog.setVisible(false);
    _dialog.dispose();
    _timerDaemonThread = null;
  }

  @Override
  public final void updateTimers()
  {
    _panel.getTotalTime().setText(Util.formatTime(_timingDelegate.getElapsedTotalTime()));
    _panel.getTableTime().setText(Util.formatTime(_timingDelegate.getElapsedProcessTime()));
  }

  private void updateMessages()
  {
    _panel.getMessages().setText(_text.toString());
  }
}
