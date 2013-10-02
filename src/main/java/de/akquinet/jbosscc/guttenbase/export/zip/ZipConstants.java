package de.akquinet.jbosscc.guttenbase.export.zip;

/**
 * Commonly used constants.
 * 
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 * 
 * @author M. Dahm
 */
public interface ZipConstants {
  String DBINFO_NAME = "DB-INFO.txt";
  String TABLE_INFO_NAME = "TABLE-INFO.txt";
  String COLUMN_NAME = "COLUMNS";
  String INDEX_NAME = "INDEXES";
  String GUTTEN_BASE_NAME = "GuttenBase";
  String TABLE_DATA_NAME = "DATA";
  char PATH_SEPARATOR = '/';
  String METADATA_NAME = "METADATA";
  String EXTRA_INFO_NAME = "EXTRA-INFO";

  String PREFIX = GUTTEN_BASE_NAME + PATH_SEPARATOR;

  String META_DATA = PREFIX + METADATA_NAME;
  String EXTRA_INFO = PREFIX + EXTRA_INFO_NAME;
  String META_INF = "META-INF";
  String MANIFEST_NAME = META_INF + PATH_SEPARATOR + "MANIFEST.MF";
}
