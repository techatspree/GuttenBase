package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapper;


/**
 * Created by mfehler on 10.04.17.
 */
public abstract class ColumnTypeMapperHint implements ConnectorHint<ColumnTypeMapper> {
    @Override
    public Class<ColumnTypeMapper> getConnectorHintType() {
        return ColumnTypeMapper.class;
    }
}
