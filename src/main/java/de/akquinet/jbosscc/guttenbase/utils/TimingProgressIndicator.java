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
  private long _startTotalTime;
  private long _startProcessTime;
  private long _startExecutionTime;
  private int _itemCounter;
  private String _sourceTableName;
  private String _targetTableName;
  private int _rowCount;
  private int _numberOfTables;
  private long _elapsedExecutionTime;
  private long _elapsedProcessTime;
  private long _elapsedTotalTime;

  @Override
  public void initializeIndicator()
  {}

  @Override
  public void startProcess(final int numberOfItems)
  {
    setNumberOfItems(numberOfItems);
    setItemCounter(1);
    setStartTotalTime(System.currentTimeMillis());
    setStartProcessTime(System.currentTimeMillis());
  }

  @Override
  public void startCopyTable(final String sourceTableName, final int rowCount, final String targetTableName)
  {
    setSourceTableName(sourceTableName);
    setRowCount(rowCount);
    setTargetTableName(targetTableName);
    setStartProcessTime(System.currentTimeMillis());
  }

  @Override
  public void startExecution()
  {
    setStartExecutionTime(System.currentTimeMillis());
  }

  @Override
  public void endExecution(final int numberOfItems)
  {
    updateTimers();
  }

  @Override
  public void endProcess()
  {
    updateTimers();
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
    updateTimers();
  }

  public final long getStartTotalTime()
  {
    return _startTotalTime;
  }

  private void setStartTotalTime(final long startCopyTotal)
  {
    _startTotalTime = startCopyTotal;
  }

  public final long getStartProcessTime()
  {
    return _startProcessTime;
  }

  private void setStartProcessTime(final long startCopyTable)
  {
    _startProcessTime = startCopyTable;
  }

  public final long getStartExecutionTime()
  {
    return _startExecutionTime;
  }

  private void setStartExecutionTime(final long startBatch)
  {
    _startExecutionTime = startBatch;
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

  public final long getElapsedExecutionTime()
  {
    return _elapsedExecutionTime;
  }

  private void setElapsedExecutionTime(final long elapsedBatchTime)
  {
    _elapsedExecutionTime = elapsedBatchTime;
  }

  public final long getElapsedProcessTime()
  {
    return _elapsedProcessTime;
  }

  private void setElapsedProcessTime(final long elapsedTableCopyTime)
  {
    _elapsedProcessTime = elapsedTableCopyTime;
  }

  public final long getElapsedTotalTime()
  {
    return _elapsedTotalTime;
  }

  @Override
  public final void updateTimers()
  {
    final long millis = System.currentTimeMillis();

    setElapsedExecutionTime(millis - getStartExecutionTime());
    setElapsedTotalTime(millis - getStartTotalTime());
    setElapsedProcessTime(millis - getStartProcessTime());
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
