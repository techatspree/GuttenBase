package de.akquinet.jbosscc.guttenbase.hints;

/**
 * Users may add configuration "hints" that influence the tools. E.g., the buffer size when reading or writing data. There is always a
 * default implementation added to a connector by the repository which may be overridden subsequently.
 * <p></p>
 * <p>
 * &copy; 2012-2020 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public interface ConnectorHint<T> {
    Class<T> getConnectorHintType();

    T getValue();
}
