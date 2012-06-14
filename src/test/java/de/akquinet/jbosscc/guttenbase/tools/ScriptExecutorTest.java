package de.akquinet.jbosscc.guttenbase.tools;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringTokenizer;

import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Before;
import org.junit.Test;

import de.akquinet.jbosscc.guttenbase.configuration.EasymockConnectionInfo;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;

public class ScriptExecutorTest {
	public static final String CONNECTOR_ID = "easymock";

	private final ConnectorRepository _connectorRepository = new ConnectorRepositoryImpl();
	private final ScriptExecutorTool _objectUnderTest = new ScriptExecutorTool(_connectorRepository);

	private final EasymockConnectionInfo _connectionInfo = new EasymockConnectionInfo();

	@Before
	public void setup() {
		_connectorRepository.addConnectionInfo(CONNECTOR_ID, _connectionInfo);
	}

	@Test
	public void testExecute() throws Exception {
		final PreparedStatement statement = _connectionInfo.getPreparedStatement();
		final String expected1 = "CREATE TABLE FOO_USER ( ID bigint  PRIMARY KEY, COMPANY_NAME VARCHAR(255) )";
		final String expected2 = "INSERT INTO FOO_USER VALUES(1, 'Un''fug')";
		final String expected3 = "SELECT * FROM FOO_USER";

		expectConnectionSetup();

		expect(statement.execute(equalSql(expected1))).andReturn(true);
		expect(statement.execute(equalSql(expected2))).andReturn(true);
		expect(statement.execute(equalSql(expected3))).andReturn(true);

		statement.close();

		_connectionInfo.replay();
		_objectUnderTest.executeFileScript(CONNECTOR_ID, true, false, "sql/test.sql");
		_connectionInfo.verify();
	}

	@Test
	public void testDDL() throws Exception {
		readFile("sql/postgresql.ddl");
	}

	@Test
	public void testFunctionScript() throws Exception {
		final PreparedStatement statement = _connectionInfo.getPreparedStatement();

		expectConnectionSetup();

		expect(statement.execute((String) anyObject())).andReturn(true); // one line

		statement.close();

		_connectionInfo.replay();
		_objectUnderTest.executeFileScript(CONNECTOR_ID, true, false, "sql/function.sql");
		_connectionInfo.verify();
	}

	private void readFile(final String fileName) throws SQLException {
		expectConnectionSetup();
		expect(_connectionInfo.getPreparedStatement().execute((String) anyObject())).andReturn(true).anyTimes();

		_connectionInfo.getPreparedStatement().close();

		_connectionInfo.replay();
		_objectUnderTest.executeFileScript(CONNECTOR_ID, true, false, fileName);
		_connectionInfo.verify();
	}

	private void expectConnectionSetup() throws SQLException {
		final Connection connection = _connectionInfo.getConnection();
		final PreparedStatement statement = _connectionInfo.getPreparedStatement();

		expect(connection.createStatement()).andReturn(statement).anyTimes();
		expect(connection.isClosed()).andReturn(false).anyTimes();
		expect(connection.getAutoCommit()).andReturn(false).anyTimes();
		connection.commit();
		connection.close();
	}

	private String equalSql(final String expected) {
		EasyMock.reportMatcher(new IArgumentMatcher() {
			@Override
			public boolean matches(final Object argument) {
				final StringTokenizer tokenizer1 = new StringTokenizer(argument.toString(), " ");
				final StringTokenizer tokenizer2 = new StringTokenizer(expected, " ");

				if (tokenizer1.countTokens() != tokenizer2.countTokens()) {
					return false;
				}

				while (tokenizer1.hasMoreTokens() && tokenizer2.hasMoreTokens()) {
					final String token1 = tokenizer1.nextToken();
					final String token2 = tokenizer2.nextToken();

					if (!token1.equals(token2)) {
						return false;
					}
				}

				return true;
			}

			@Override
			public void appendTo(final StringBuffer buffer) {
			}
		});

		return null;
	}
}
