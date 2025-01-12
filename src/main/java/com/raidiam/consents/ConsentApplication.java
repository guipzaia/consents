package com.raidiam.consents;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class ConsentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsentApplication.class, args);
	}

	/**
	 * Set UTC timezone
	 */
	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
}
