package de.akquinet.jbosscc.guttenbase.utils;

import org.apache.log4j.Logger;

public class LoggingProgressIndicator implements ProgressIndicator
{
  private static final Logger LOG = Logger.getLogger(LoggingProgressIndicator.class);

  private long _startCopyTotal;
  private long _startCopyTable;
  private long _startBatch;
  private int _tableCounter;
  private String _sourceTableName;
  private String _targetTableName;
  private int _rowCount;
  private int _numberOfTables;

  @Override
  public void initializeIndicator()
  {}

  @Override
  public void startCopying(final int numberOfTables)
  {
    _numberOfTables = numberOfTables;
    _tableCounter = 1;
    _startCopyTotal = System.currentTimeMillis();
  }

  @Override
  public void startCopyTable(final String sourceTableName, final int rowCount, final String targetTableName,
      final int numberOfRowsPerBatch)
  {
    _sourceTableName = sourceTableName;
    _rowCount = rowCount;
    _targetTableName = targetTableName;
    _startCopyTable = System.currentTimeMillis();

    LOG.info("Copying of " + _sourceTableName + " -> " + _targetTableName + "(" + _tableCounter + "/" + rowCount + ") started");
  }

  @Override
  public void startBatch()
  {
    _startBatch = System.currentTimeMillis();
  }

  @Override
  public void endBatch(final int totalCopiedRows)
  {
    final long elapsedTime = System.currentTimeMillis() - _startBatch;

    LOG.info(_sourceTableName + ":"
        + totalCopiedRows
        + "/"
        + _rowCount
        + " lines copied: Last batch took "
        + Util.formatTime(elapsedTime));
  }

  @Override
  public void endCopyTable()
  {
    final long elapsedTime = System.currentTimeMillis() - _startCopyTable;

    LOG.info("Copying of " + _sourceTableName + " -> " + _targetTableName + " took " + Util.formatTime(elapsedTime));
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
    final long elapsedTime = System.currentTimeMillis() - _startCopyTotal;
    LOG.info("Copying of " + _numberOfTables + " tables took " + Util.formatTime(elapsedTime));
  }
}
