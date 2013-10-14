package de.akquinet.jbosscc.guttenbase.utils;

/**
 * Show progress when copying tables. Simple implementation will just log to console.
 * <p>
 * &copy; 2013-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface TableCopyProgressIndicator extends ProgressIndicator
{
  void startCopyTable(String sourceTableName, int rowCount, String targetTableName, int numberOfRowsPerBatch);
}
