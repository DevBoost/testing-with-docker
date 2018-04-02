package de.devboost.dockertesting.restservice;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import static io.restassured.RestAssured.when;

public class HealthControllerIT {

	private static final Logger LOGGER = LoggerFactory.getLogger("APP");

	private final Network network = Network.builder()
			.createNetworkCmdModifier(cmd -> cmd.withName("test-network"))
			.build();

	private final GenericContainer mysqlContainer = new GenericContainer("mysql:5.7.21")
			.withEnv("MYSQL_ROOT_PASSWORD", "secret")
			.withEnv("MYSQL_DATABASE", "testdb")
			.withNetwork(network)
			.withExposedPorts(3306)
			.withNetworkAliases("mysql");

	private final GenericContainer applicationContainer = new GenericContainer(
			"devboost.de/github/dockertesting/restservice:0.0.1-SNAPSHOT")
			.withNetwork(network)
			.withExposedPorts(8080)
			.withLogConsumer(new Slf4jLogConsumer(LOGGER));

	private final ExternalResource applicationConfigurator = new ExternalResource() {

		@Override
		protected void before() throws Throwable {
			applicationContainer.withEnv("SPRING_DATASOURCE_URL", "jdbc:mysql://mysql:3306/testdb?useSSL=false")
					.withEnv("SPRING_DATASOURCE_USERNAME", "root")
					.withEnv("SPRING_DATASOURCE_PASSWORD", "secret");
			super.before();
		}
	};

	@Rule
	public final RuleChain allContainers = RuleChain.emptyRuleChain()
			.around(network)
			.around(mysqlContainer)
			.around(applicationConfigurator)
			.around(applicationContainer);

	@Test
	public void healthRequiresWorkingDatabaseConnection() {
		String baseUrl = "http://localhost:" + applicationContainer.getMappedPort(8080);
		// Verify that container is healthy after startup (when there is still a MySQL connection)
		when().get(baseUrl + "/api/readiness").then().statusCode(200);

		// Stop MySQL container to simulate outage
		mysqlContainer.stop();

		// Verify that container is not healthy when there is no MySQL anymore
		when().get(baseUrl + "/api/readiness").then().statusCode(500);
	}
}
