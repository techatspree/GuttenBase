package de.akquinet.jbosscc.guttenbase.configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.easymock.EasyMock;

import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;

public class EasymockConnectionInfo extends URLConnectorInfoImpl {
	private static final long serialVersionUID = 1L;

	private final Connection _connection = EasyMock.createMock(Connection.class);
	private final PreparedStatement _preparedStatement = EasyMock.createMock(PreparedStatement.class);

	public EasymockConnectionInfo() {
		super("anything", "anything", "anything", "de.akquinet.jbosscc.guttenbase.configuration.EasymockDriver", "", DatabaseType.MOCK);
		EasymockDriver.INSTANCE.setInfo(this);
	}

	public PreparedStatement getPreparedStatement() {
		return _preparedStatement;
	}

	public Connection getConnection() {
		return _connection;
	}

	public void replay() {
		EasyMock.replay(_connection, _preparedStatement);
	}

	public void verify() {
		EasyMock.verify(_connection, _preparedStatement);
	}

	public void reset() {
		EasyMock.reset(_connection, _preparedStatement);
	}
}
