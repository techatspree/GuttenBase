package de.akquinet.jbosscc.guttenbase.export;

import org.apache.commons.io.IOUtils;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import java.io.*;
import java.sql.SQLException;
import java.sql.SQLXML;

/**
 * Since XML data may be quite big. we do not load them into memory completely,
 * but read them in chunks and write the data to the output stream in a loop.
 *
 * <p>
 * &copy; 2012-2034 akquinet tech@spree
 * </p>
 *
 * @author M. Dahm
 */
public class ExportDumpSqlXML extends AbstractExportDumpObject implements SQLXML {
  private static final long serialVersionUID = 1L;

  public ExportDumpSqlXML() {
    this(null);
  }

  public ExportDumpSqlXML(final InputStream inputStream) {
    super(inputStream);
  }

  @Override
  public Reader getCharacterStream() throws SQLException {
    return new InputStreamReader(getBinaryStream());
  }

  @Override
  public OutputStream setBinaryStream() throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Writer setCharacterStream() throws SQLException {
    throw new UnsupportedOperationException();

  }

  @Override
  public String getString() throws SQLException {
    try {
      return IOUtils.toString(getBinaryStream());
    } catch (final IOException e) {
      throw new SQLException("getString", e);
    }
  }

  @Override
  public void setString(final String value) throws SQLException {
    throw new UnsupportedOperationException();

  }

  @Override
  public <T extends Source> T getSource(final Class<T> sourceClass) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T extends Result> T setResult(final Class<T> resultClass) throws SQLException {
    throw new UnsupportedOperationException();
  }
}
