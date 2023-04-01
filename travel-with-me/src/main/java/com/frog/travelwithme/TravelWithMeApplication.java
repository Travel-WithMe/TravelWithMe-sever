package com.frog.travelwithme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TravelWithMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelWithMeApplication.class, args);
	}

}
