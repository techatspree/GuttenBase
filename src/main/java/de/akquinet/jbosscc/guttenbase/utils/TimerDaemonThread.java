package de.akquinet.jbosscc.guttenbase.utils;

import javax.swing.JDialog;

public class TimerDaemonThread extends Thread
{
  private boolean _active = true;
  private final JDialog _dialog;
  private final ProgressIndicator[] _progressIndicators;

  public TimerDaemonThread(final JDialog dialog, final ProgressIndicator... progressIndicators)
  {
    super("GB-Timer-Daemon");
    setDaemon(true);

    _progressIndicators = progressIndicators;
    _dialog = dialog;
  }

  @Override
  public void run()
  {
    _dialog.setVisible(true);

    while (_active && _dialog.isVisible())
    {
      try
      {
        Thread.sleep(800L);
      }
      catch (final InterruptedException ignored)
      {}

      for (final ProgressIndicator progressIndicator : _progressIndicators)
      {
        progressIndicator.updateTimers();
      }
    }
  }

  public boolean isActive()
  {
    return _active;
  }

  public void setActive(final boolean active)
  {
    _active = active;
  }
}
