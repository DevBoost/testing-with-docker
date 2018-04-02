package de.devboost.dockertesting.restservice;

import javax.persistence.*;

@SuppressWarnings("WeakerAccess")
@Entity
@Table(name = "entity")
public class SimpleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
