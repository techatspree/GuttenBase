package de.akquinet.jbosscc.guttenbase.tools.postgresql;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.AbstractSequenceUpdateTool;

/**
 * Usually Postgresql creates an autoincrement ID sequence for tables. After data migration these sequences need to be updated...
 * <p>
 * By default the sequence is updated to SELECT(MAX(ID) + 1) FROM table
 *
 * <p>&copy; 2012-2020 akquinet tech@spree</p>
 *
 * @author M. Dahm
 */
public class PostgresqlSequenceUpdateTool extends AbstractSequenceUpdateTool {
  public PostgresqlSequenceUpdateTool(final ConnectorRepository connectorRepository) {
    super(connectorRepository);
  }

  @Override
  public String getSequenceName(final String tableName) {
    return tableName + "_id_seq";
  }

  @Override
  public String getUpdateSequenceClause(final String sequenceName, final long sequenceValue) {
    return "SELECT setval('" + sequenceName + "', " + sequenceValue + ", true);";
  }
}
