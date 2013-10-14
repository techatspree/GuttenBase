package de.akquinet.jbosscc.guttenbase.utils;

/**
 * Record timings.
 * <p>
 * &copy; 2013-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public class TimingProgressIndicator implements TableCopyProgressIndicator
{
  private long _startCopyTotal;
  private long _startCopyTable;
  private long _startBatch;
  private int _itemCounter;
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
  public void startProcess(final int numberOfItems)
  {
    setNumberOfItems(numberOfItems);
    setItemCounter(1);
    setStartCopyTotal(System.currentTimeMillis());
    setStartCopyTable(System.currentTimeMillis());
  }

  @Override
  public void startCopyTable(final String sourceTableName, final int rowCount, final String targetTableName,
      final int numberOfRowsPerBatch)
  {
    setSourceTableName(sourceTableName);
    setRowCount(rowCount);
    setTargetTableName(targetTableName);
    setStartCopyTable(System.currentTimeMillis());
  }

  @Override
  public void startExecution()
  {
    setStartBatch(System.currentTimeMillis());
  }

  @Override
  public void endExecution(final int totalCopiedRows)
  {
    updateTimers();
  }

  @Override
  public void endProcess()
  {
    setElapsedTableCopyTime(System.currentTimeMillis() - getStartCopyTable());

    _itemCounter++;
  }

  @Override
  public void warn(final String text)
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
    setElapsedTotalTime(System.currentTimeMillis() - getStartCopyTotal());
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

  private void setNumberOfItems(final int numberOfTables)
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

  @Override
  public final void updateTimers()
  {
    setElapsedBatchTime(System.currentTimeMillis() - getStartBatch());
    setElapsedTotalTime(System.currentTimeMillis() - getStartCopyTotal());
    setElapsedTableCopyTime(System.currentTimeMillis() - getStartCopyTable());
  }

  private void setElapsedTotalTime(final long elapsedTotalTime)
  {
    _elapsedTotalTime = elapsedTotalTime;
  }

  public final int getItemCounter()
  {
    return _itemCounter;
  }

  private void setItemCounter(final int tableCounter)
  {
    _itemCounter = tableCounter;
  }
}
