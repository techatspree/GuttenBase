package de.akquinet.jbosscc.guttenbase.utils;

import org.apache.log4j.Logger;

public class LoggingScriptExecutorProgressIndicator implements ScriptExecutorProgressIndicator
{
  private static final Logger LOG = Logger.getLogger(LoggingScriptExecutorProgressIndicator.class);

  private final TimingProgressIndicator _timingDelegate = new TimingProgressIndicator();

  @Override
  public void initializeIndicator()
  {
    _timingDelegate.initializeIndicator();
  }

  @Override
  public void startProcess(final int numberOfTables)
  {
    _timingDelegate.startProcess(numberOfTables);
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
  }

  @Override
  public void endProcess()
  {
    _timingDelegate.endProcess();
  }

  @Override
  public void warn(final String text)
  {
    _timingDelegate.warn(text);
    LOG.warn(text);
  }

  @Override
  public void info(final String text)
  {
    _timingDelegate.info(text);
    LOG.info(text);
  }

  @Override
  public void debug(final String text)
  {
    _timingDelegate.debug(text);
    LOG.debug(text);
  }

  @Override
  public void finalizeIndicator()
  {
    _timingDelegate.finalizeIndicator();
  }

  @Override
  public void updateTimers()
  {
    throw new UnsupportedOperationException();
  }
}
