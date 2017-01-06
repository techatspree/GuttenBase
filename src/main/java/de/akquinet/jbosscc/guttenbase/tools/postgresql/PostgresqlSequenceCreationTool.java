package de.akquinet.jbosscc.guttenbase.tools.postgresql;

import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.AbstractSequenceCreationTool;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Create an autoincrement ID sequence for tables.
 *
 *
 * <p>&copy; 2012-2020 akquinet tech@spree</p>
 *
 * @author M. Dahm
 */
public class PostgresqlSequenceCreationTool extends AbstractSequenceCreationTool
{
  public PostgresqlSequenceCreationTool(final ConnectorRepository connectorRepository)
  {
    super(connectorRepository);
  }

  @Override
  protected String getIdColumn(final TableMetaData tableMetaData)
  {
    return "ID";
  }

  @Override
  public List<String> getCreateSequenceClauses(final String tableName, String idColumn, final String sequenceName, final long start, final long incrementBy)
  {
    return Arrays.asList("CREATE SEQUENCE " + sequenceName +
                    "    START WITH " + start +
                    "    INCREMENT BY " + incrementBy +
                    "    NO MINVALUE\n" +
                    "    NO MAXVALUE\n" +
                    "    CACHE 1;",
            "ALTER SEQUENCE " + sequenceName + " OWNED BY " + tableName + "." + idColumn + ";");
  }

  @Override
  public String getSequenceName(final String tableName) throws SQLException
  {
    return tableName + "_id_seq";
  }
}
