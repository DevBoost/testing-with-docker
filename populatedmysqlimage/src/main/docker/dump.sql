CREATE DATABASE mytestdatabase;
USE mytestdatabase;

CREATE TABLE users (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	email VARCHAR(100) NOT NULL,
	creation_date DATE NOT NULL,
	PRIMARY KEY ( id )
);

INSERT INTO users (id, name, email, creation_date) VALUES (1, "User 1", "user1@devboost.de", NOW());
