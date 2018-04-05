package de.devboost.dockertesting.restserviceconsumer;

import de.devboost.dockertesting.restservice.testsetup.SimpleRestServiceSetup;
import org.junit.Rule;
import org.junit.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class RestConsumerTest {

	@Rule
	public final SimpleRestServiceSetup restServiceSetup = new SimpleRestServiceSetup();

	@Test
	public void canMakeCallsToSimpleRestService() {
		String baseUrl = restServiceSetup.getBaseUrl();

		// Verify that connection to simple REST service works
		when().get(baseUrl + "/api/count").then().statusCode(200).body(equalTo("0"));
		when().get(baseUrl + "/api/create").then().statusCode(200);
		when().get(baseUrl + "/api/count").then().statusCode(200).body(equalTo("1"));
		when().get(baseUrl + "/api/create").then().statusCode(200);
		when().get(baseUrl + "/api/count").then().statusCode(200).body(equalTo("2"));
	}
}
