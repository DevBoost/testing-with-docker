package de.devboost.dockertesting.restservice;

import org.springframework.data.repository.CrudRepository;

public interface SimpleRepository extends CrudRepository<SimpleEntity, Long> {
}
