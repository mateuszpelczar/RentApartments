package com.example.RentApartments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.RentApartments.repository.jpa")
@EnableMongoRepositories(basePackages = "com.example.RentApartments.repository.mongo")
public class RentApartmentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentApartmentsApplication.class, args);
	}
	}

