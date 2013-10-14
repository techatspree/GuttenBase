package de.akquinet.jbosscc.guttenbase.utils;

/**
 * Common interface.
 * <p>
 * &copy; 2013-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface ProgressIndicator
{
  void initializeIndicator();

  void startProcess(int numberOfItems);

  void startExecution();

  void endExecution(int numberOfFinishedItems);

  void endProcess();

  void updateTimers();

  void finalizeIndicator();

  void warn(String string);

  void info(String text);

  void debug(String text);
}
