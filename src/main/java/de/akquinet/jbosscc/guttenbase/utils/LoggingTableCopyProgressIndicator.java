package de.akquinet.jbosscc.guttenbase.utils;

import org.apache.log4j.Logger;

public class LoggingTableCopyProgressIndicator implements TableCopyProgressIndicator {
  private static final Logger LOG = Logger.getLogger(LoggingTableCopyProgressIndicator.class);

  private final TimingProgressIndicator _timingDelegate = new TimingProgressIndicator();

  @Override
  public void initializeIndicator() {
    _timingDelegate.initializeIndicator();
  }

  @Override
  public void startProcess(final int numberOfTables) {
    _timingDelegate.startProcess(numberOfTables);
  }

  @Override
  public void startCopyTable(final String sourceTableName, final int rowCount, final String targetTableName) {
    _timingDelegate.startCopyTable(sourceTableName, rowCount, targetTableName);

    LOG.info("Copying of " + _timingDelegate.getSourceTableName()
      + " -> "
      + _timingDelegate.getTargetTableName()
      + "("
      + _timingDelegate.getItemCounter()
      + "/"
      + rowCount
      + ") started");
  }

  @Override
  public void startExecution() {
    _timingDelegate.startExecution();
  }

  @Override
  public void endExecution(final int totalCopiedRows) {
    _timingDelegate.endExecution(totalCopiedRows);

    final String batchTime = Util.formatTime(_timingDelegate.getElapsedExecutionTime());
    final String tableTime = Util.formatTime(_timingDelegate.getElapsedProcessTime());
    final String totalTime = Util.formatTime(_timingDelegate.getElapsedTotalTime());

    LOG.info(_timingDelegate.getSourceTableName() + ":"
      + totalCopiedRows + "/" + _timingDelegate.getRowCount()
      + " lines copied."
      + " Last batch took: " + batchTime
      + " Table time spent: " + tableTime
      + " Total time spent: " + totalTime);
  }

  @Override
  public void endProcess() {
    _timingDelegate.endProcess();

    LOG.info("Copying of " + _timingDelegate.getSourceTableName()
      + " -> "
      + _timingDelegate.getTargetTableName()
      + " took "
      + Util.formatTime(_timingDelegate.getElapsedProcessTime()));
  }

  @Override
  public void warn(final String text) {
    _timingDelegate.warn(text);
    LOG.warn(text);
  }

  @Override
  public void info(final String text) {
    _timingDelegate.info(text);
    LOG.info(text);
  }

  @Override
  public void debug(final String text) {
    _timingDelegate.debug(text);
    LOG.debug(text);
  }

  @Override
  public void finalizeIndicator() {
    _timingDelegate.finalizeIndicator();
    LOG.info("Copying of " + _timingDelegate.getNumberOfTables()
      + " tables took "
      + Util.formatTime(_timingDelegate.getElapsedTotalTime()));
  }

  @Override
  public void updateTimers() {
    throw new UnsupportedOperationException();
  }
}
