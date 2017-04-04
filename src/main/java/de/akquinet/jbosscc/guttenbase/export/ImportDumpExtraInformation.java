package de.akquinet.jbosscc.guttenbase.export;

import java.io.Serializable;
import java.util.Map;

/**
 * Give the user a possibility to retrieve extra informations from the dumped data.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
@SuppressWarnings("RedundantThrows")
public interface ImportDumpExtraInformation {
	void processExtraInformation(Map<String, Serializable> extraInformation) throws Exception;
}
