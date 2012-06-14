package de.akquinet.jbosscc.guttenbase.tools;

/**
 * How many data items may the INSERT statement have in total. I.e., how many '?' placeholders does the database support in a single
 * statement.
 * 
 * <p>
 * &copy; 2012 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface MaxNumberOfDataItems {
  int getMaxNumberOfDataItems();
}
