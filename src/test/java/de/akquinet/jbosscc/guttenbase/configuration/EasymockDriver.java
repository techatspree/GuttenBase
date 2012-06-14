package de.akquinet.jbosscc.guttenbase.configuration;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

public class EasymockDriver implements Driver {
  public static final EasymockDriver INSTANCE = new EasymockDriver();
  public EasymockConnectionInfo _info;
  private static boolean _registered;

  static {
    load();
  }

  /**
   * INTERNAL
   */
  public static Driver load() {
    try {
      if (!_registered) {
        _registered = true;
        DriverManager.registerDriver(INSTANCE);
      }
    } catch (final SQLException e) {
      e.printStackTrace();
    }

    return INSTANCE;
  }

  public EasymockConnectionInfo getInfo() {
    return _info;
  }

  public void setInfo(final EasymockConnectionInfo info) {
    _info = info;
  }

  @Override
  public Connection connect(final String url, final Properties info) throws SQLException {
    return _info.getConnection();
  }

  @Override
  public boolean acceptsURL(final String url) throws SQLException {
    return true;
  }

  @Override
  public DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) throws SQLException {
    return new DriverPropertyInfo[0];
  }

  @Override
  public int getMajorVersion() {
    return 0;
  }

  @Override
  public int getMinorVersion() {
    return 0;
  }

  @Override
  public boolean jdbcCompliant() {
    return true;
  }
}
