package com.appmartin.desmartin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DesmartinApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesmartinApplication.class, args);
	}
}
