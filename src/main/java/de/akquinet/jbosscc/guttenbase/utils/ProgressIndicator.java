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
  void initializeIndicator();

  void startCopying(int numberOfTables);

  void startCopyTable(String sourceTableName, int rowCount, String targetTableName, int numberOfRowsPerBatch);

  void startBatch();

  void endBatch(int totalCopiedRows);

  void endCopyTable();

  void warn(String string);

  void info(String text);

  void debug(String text);

  void finalizeIndicator();

}
