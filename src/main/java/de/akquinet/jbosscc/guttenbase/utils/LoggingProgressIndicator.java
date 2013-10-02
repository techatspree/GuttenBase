package de.akquinet.jbosscc.guttenbase.utils;

import org.apache.log4j.Logger;

public class LoggingProgressIndicator implements ProgressIndicator
{
  private static final Logger LOG = Logger.getLogger(LoggingProgressIndicator.class);

  private final TimingProgressIndicator _timingDelegate = new TimingProgressIndicator();

  @Override
  public void initializeIndicator()
  {}

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

    LOG.info("Copying of " + _timingDelegate.getSourceTableName()
        + " -> "
        + _timingDelegate.getTargetTableName()
        + "("
        + _timingDelegate.getTableCounter()
        + "/"
        + rowCount
        + ") started");
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

    LOG.info(_timingDelegate.getSourceTableName() + ":"
        + totalCopiedRows
        + "/"
        + _timingDelegate.getRowCount()
        + " lines copied: Last batch took "
        + Util.formatTime(_timingDelegate.getElapsedBatchTime()));
  }

  @Override
  public void endCopyTable()
  {
    _timingDelegate.endCopyTable();

    LOG.info("Copying of " + _timingDelegate.getSourceTableName()
        + " -> "
        + _timingDelegate.getTargetTableName()
        + " took "
        + Util.formatTime(_timingDelegate.getElapsedTableCopyTime()));
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
    LOG.info("Copying of " + _timingDelegate.getNumberOfTables()
        + " tables took "
        + Util.formatTime(_timingDelegate.getElapsedTotalTime()));
  }
}
