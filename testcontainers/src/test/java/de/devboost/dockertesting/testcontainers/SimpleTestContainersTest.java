package de.devboost.dockertesting.testcontainers;

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
			.withExposedPorts(3306);

	@Test
	public void canStartAndStopMySqlContainer() {
		LOGGER.info("Container ID is {}", mysqlContainer.getContainerId());
	}
}
