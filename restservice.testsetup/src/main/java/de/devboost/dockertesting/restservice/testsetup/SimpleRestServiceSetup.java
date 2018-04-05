package de.devboost.dockertesting.restservice.testsetup;

import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;

/**
 * The {@link SimpleRestServiceSetup} demonstrates how to package all containers required to run the simple REST
 * application as a JUnit {@link TestRule}.
 */
public class SimpleRestServiceSetup implements TestRule {

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

	private final RuleChain allContainers = RuleChain.emptyRuleChain()
			.around(network)
			.around(mysqlContainer)
			.around(applicationConfigurator)
			.around(applicationContainer);

	@Override
	public Statement apply(Statement base, Description description) {
		return allContainers.apply(base, description);
	}

	private int getMappedPort() {
		return applicationContainer.getMappedPort(8080);
	}

	public String getBaseUrl() {
		return "http://localhost:" + getMappedPort();
	}
}
