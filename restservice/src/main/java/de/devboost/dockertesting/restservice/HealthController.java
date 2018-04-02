package de.devboost.dockertesting.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

	@Autowired
	private SimpleRepository simpleRepository;

	@RequestMapping("/liveness")
	public ResponseEntity liveness() {
		return ResponseEntity.ok().build();
	}

	@RequestMapping("/readiness")
	public ResponseEntity readiness() {
		try {
			// execute a database action
			simpleRepository.count();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok().build();
	}
}
