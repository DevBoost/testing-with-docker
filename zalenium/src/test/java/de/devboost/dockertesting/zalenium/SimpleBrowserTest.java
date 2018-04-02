package de.devboost.dockertesting.zalenium;

import java.io.File;
import java.net.URL;
import java.util.function.Consumer;

import com.github.dockerjava.api.command.CreateContainerCmd;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.LogMessageWaitStrategy;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class SimpleBrowserTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleBrowserTest.class);

	private static final Logger ZALENIUM_LOGGER = LoggerFactory.getLogger("Zalenium");

	@SuppressWarnings("Convert2Lambda")
	@Rule
	public final GenericContainer zaleniumContainer =
			new GenericContainer("dosel/zalenium:3.11.0c")
					.withFileSystemBind("/var/run/docker.sock", "/var/run/docker.sock", BindMode.READ_WRITE)
					.withFileSystemBind(new File("videos").getAbsolutePath(),
							"/home/seluser/videos",
							BindMode.READ_WRITE)
					.withPrivilegedMode(true)
					.withCreateContainerCmdModifier(new Consumer<CreateContainerCmd>() {

						public void accept(CreateContainerCmd createContainerCmd) {
							createContainerCmd.withName("zalenium");
						}
					})
					.withCommand("start")
					.withLogConsumer(new Slf4jLogConsumer(ZALENIUM_LOGGER))
					.withExposedPorts(4444)
					.waitingFor(new LogMessageWaitStrategy().withRegEx(".*Zalenium is now ready.*\n"));

	@Test
	public void canPerformRequestToGoogle() throws Exception {
		FirefoxOptions options = new FirefoxOptions();
		options.setCapability("name", SimpleBrowserTest.class.getSimpleName());
		String remoteDriverUrl = "http://localhost:"
				+ zaleniumContainer.getMappedPort(4444)
				+ "/wd/hub";

		RemoteWebDriver driver = new RemoteWebDriver(new URL(remoteDriverUrl), options);
		driver.get("https://www.google.com");
		LOGGER.info("Title is {}", driver.getTitle());
		driver.quit();
		await().atMost(30, SECONDS).until(this::isVideoPresent);
	}

	private boolean isVideoPresent() {
		File[] videos = new File("videos").listFiles((dir, name) -> name.endsWith(".mp4"));
		if (videos == null) {
			return false;
		}
		return videos.length > 0;
	}
}
