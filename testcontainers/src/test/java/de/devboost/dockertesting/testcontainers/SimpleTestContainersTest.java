package de.devboost.dockertesting.testcontainers;

import java.sql.*;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;

public class SimpleTestContainersTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTestContainersTest.class);

	@Rule
	public GenericContainer mysqlContainer = new GenericContainer("mysql:5.7.21")
			.withEnv("MYSQL_ROOT_PASSWORD", "secret")
			.withEnv("MYSQL_DATABASE", "testdb")
			.withExposedPorts(3306);

	@Test
	public void canStartAndStopMySqlContainer() {
		LOGGER.info("Container ID is {}", mysqlContainer.getContainerId());
	}

	@Test
	public void canListDatabaseTables() throws SQLException {
		int mappedPort = mysqlContainer.getMappedPort(3306);
		String connectionUrl = "jdbc:mysql://localhost:" + mappedPort + "/testdb";
		try (Connection connection = DriverManager.getConnection(connectionUrl, "root", "secret")) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SHOW TABLES;");
			while (resultSet.next()) {
				LOGGER.info("Found table '{}'", resultSet.getString(1));
			}
		}
	}
}
