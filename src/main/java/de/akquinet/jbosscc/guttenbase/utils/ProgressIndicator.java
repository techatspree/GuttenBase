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

  void updateTimers();

  void finalizeIndicator();
}
