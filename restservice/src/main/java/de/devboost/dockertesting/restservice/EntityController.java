package de.devboost.dockertesting.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EntityController {

	@Autowired
	private SimpleRepository simpleRepository;

	@RequestMapping("/count")
	public ResponseEntity<Integer> count() {
		return new ResponseEntity<>((int) simpleRepository.count(), HttpStatus.OK);
	}

	@RequestMapping("/create")
	public ResponseEntity create() {
		simpleRepository.save(new SimpleEntity());
		return ResponseEntity.ok().build();
	}
}
