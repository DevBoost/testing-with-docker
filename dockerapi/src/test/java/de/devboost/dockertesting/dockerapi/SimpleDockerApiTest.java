package de.devboost.dockertesting.dockerapi;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;

public class SimpleDockerApiTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDockerApiTest.class);

	@Test
	public void canStartAndStopMySqlContainer() throws Exception {
		// Create Docker client
		DockerClient dockerClient = DefaultDockerClient.fromEnv().build();

		// Pull the MySQL image
		String imageName = "mysql:5.7.21";
		dockerClient.pull(imageName);

		// Create a MySQL container with exposed port
		ContainerConfig containerConfig = ContainerConfig.builder()
				.image(imageName)
				.env("MYSQL_ROOT_PASSWORD=secret")
				.exposedPorts("3306")
				.build();

		ContainerCreation creation = dockerClient.createContainer(containerConfig);
		String containerId = creation.id();
		LOGGER.info("Container ID is {}", containerId);
		assertNotNull("Created container must have an ID", containerId);
		dockerClient.startContainer(containerId);

		// Stop and remove container
		dockerClient.stopContainer(containerId, 30);
		dockerClient.removeContainer(containerId);
	}
}
