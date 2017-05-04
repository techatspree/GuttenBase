package de.akquinet.jbosscc.guttenbase.hints;

import de.akquinet.jbosscc.guttenbase.mapping.CustomColumnTypeMapper;


/**
 * Created by mfehler on 10.04.17.
 */
public abstract class CustomColumnTypeMapperHint implements ConnectorHint<CustomColumnTypeMapper> {
    @Override
    public Class<CustomColumnTypeMapper> getConnectorHintType() {
        return CustomColumnTypeMapper.class;
    }
}
