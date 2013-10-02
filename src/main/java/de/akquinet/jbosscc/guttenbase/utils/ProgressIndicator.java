package de.akquinet.jbosscc.guttenbase.utils;

/**
 * Show progress when copying tables. Simple implementations will just log to console.
 * <p>
 * &copy; 2013-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface ProgressIndicator
{
  void init(int numberOfTables);

  void nextTable(String sourceTable, String targetTable);

  void batchDone(long timeMillis);

  void log(String text);
}
