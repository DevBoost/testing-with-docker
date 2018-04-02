package de.devboost.dockertesting.populatedmysqlimage;

import java.sql.*;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;

public class SimplePopulatedImageTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimplePopulatedImageTest.class);

	@Rule
	public GenericContainer mysqlContainer = new GenericContainer(
			"devboost.de/github/dockertesting/populatedmysqlimage:0.0.1-SNAPSHOT")
			.withExposedPorts(3306);

	@Test
	public void canStartAndStopMySqlContainer() {
		LOGGER.info("Container ID is {}", mysqlContainer.getContainerId());
	}

	@Test
	public void canListUsers() throws SQLException {
		int mappedPort = mysqlContainer.getMappedPort(3306);
		String connectionUrl = "jdbc:mysql://localhost:" + mappedPort + "/mytestdatabase";
		try (Connection connection = DriverManager.getConnection(connectionUrl, "root", "secret")) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT name from users;");
			while (resultSet.next()) {
				LOGGER.info("Found user '{}'", resultSet.getString(1));
			}
		}
	}
}
