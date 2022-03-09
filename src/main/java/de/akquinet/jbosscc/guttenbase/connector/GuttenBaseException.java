package de.akquinet.jbosscc.guttenbase.connector;

public class GuttenBaseException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public GuttenBaseException(String message) {
    super(message);
  }

  public GuttenBaseException(String message, Throwable cause) {
    super(message, cause);
  }
}
