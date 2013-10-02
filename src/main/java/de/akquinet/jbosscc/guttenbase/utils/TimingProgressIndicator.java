package de.akquinet.jbosscc.guttenbase.utils;

import org.apache.log4j.Logger;

public class TimingProgressIndicator implements ProgressIndicator
{
  private static final Logger LOG = Logger.getLogger(TimingProgressIndicator.class);

  private long _startCopyTotal;
  private long _startCopyTable;
  private long _startBatch;
  private int _tableCounter;
  private String _sourceTableName;
  private String _targetTableName;
  private int _rowCount;
  private int _numberOfTables;
  private long _elapsedBatchTime;
  private long _elapsedTableCopyTime;
  private long _elapsedTotalTime;

  @Override
  public void initializeIndicator()
  {}

  @Override
  public void startCopying(final int numberOfTables)
  {
    setNumberOfTables(numberOfTables);
    setTableCounter(1);
    setStartCopyTotal(System.currentTimeMillis());
  }

  @Override
  public void startCopyTable(final String sourceTableName, final int rowCount, final String targetTableName,
      final int numberOfRowsPerBatch)
  {
    setSourceTableName(sourceTableName);
    setRowCount(rowCount);
    setTargetTableName(targetTableName);
    setStartCopyTable(System.currentTimeMillis());

    LOG.info("Copying of " + getSourceTableName()
        + " -> "
        + getTargetTableName()
        + "("
        + getTableCounter()
        + "/"
        + rowCount
        + ") started");
  }

  @Override
  public void startBatch()
  {
    setStartBatch(System.currentTimeMillis());
  }

  @Override
  public void endBatch(final int totalCopiedRows)
  {
    setElapsedBatchTime(System.currentTimeMillis() - getStartBatch());

    LOG.info(getSourceTableName() + ":"
        + totalCopiedRows
        + "/"
        + getRowCount()
        + " lines copied: Last batch took "
        + Util.formatTime(getElapsedBatchTime()));
  }

  @Override
  public void endCopyTable()
  {
    setElapsedTableCopyTime(System.currentTimeMillis() - getStartCopyTable());

    LOG.info("Copying of " + getSourceTableName()
        + " -> "
        + getTargetTableName()
        + " took "
        + Util.formatTime(getElapsedTableCopyTime()));

    _tableCounter++;
  }

  @Override
  public void warn(final String text)
  {
    LOG.warn(text);
  }

  @Override
  public void info(final String text)
  {
    LOG.info(text);
  }

  @Override
  public void debug(final String text)
  {
    LOG.debug(text);
  }

  @Override
  public void finalizeIndicator()
  {
    setElapsedTotalTime(System.currentTimeMillis() - getStartCopyTotal());
    LOG.info("Copying of " + getNumberOfTables() + " tables took " + Util.formatTime(getElapsedTotalTime()));
  }

  public final long getStartCopyTotal()
  {
    return _startCopyTotal;
  }

  private void setStartCopyTotal(final long startCopyTotal)
  {
    _startCopyTotal = startCopyTotal;
  }

  public final long getStartCopyTable()
  {
    return _startCopyTable;
  }

  private void setStartCopyTable(final long startCopyTable)
  {
    _startCopyTable = startCopyTable;
  }

  public final long getStartBatch()
  {
    return _startBatch;
  }

  private void setStartBatch(final long startBatch)
  {
    _startBatch = startBatch;
  }

  public final String getSourceTableName()
  {
    return _sourceTableName;
  }

  private void setSourceTableName(final String sourceTableName)
  {
    _sourceTableName = sourceTableName;
  }

  public final String getTargetTableName()
  {
    return _targetTableName;
  }

  private void setTargetTableName(final String targetTableName)
  {
    _targetTableName = targetTableName;
  }

  public final int getRowCount()
  {
    return _rowCount;
  }

  private void setRowCount(final int rowCount)
  {
    _rowCount = rowCount;
  }

  public final int getNumberOfTables()
  {
    return _numberOfTables;
  }

  private void setNumberOfTables(final int numberOfTables)
  {
    _numberOfTables = numberOfTables;
  }

  public final long getElapsedBatchTime()
  {
    return _elapsedBatchTime;
  }

  private void setElapsedBatchTime(final long elapsedBatchTime)
  {
    _elapsedBatchTime = elapsedBatchTime;
  }

  public final long getElapsedTableCopyTime()
  {
    return _elapsedTableCopyTime;
  }

  private void setElapsedTableCopyTime(final long elapsedTableCopyTime)
  {
    _elapsedTableCopyTime = elapsedTableCopyTime;
  }

  public final long getElapsedTotalTime()
  {
    return _elapsedTotalTime;
  }

  private void setElapsedTotalTime(final long elapsedTotalTime)
  {
    _elapsedTotalTime = elapsedTotalTime;
  }

  public final int getTableCounter()
  {
    return _tableCounter;
  }

  private void setTableCounter(final int tableCounter)
  {
    _tableCounter = tableCounter;
  }
}
