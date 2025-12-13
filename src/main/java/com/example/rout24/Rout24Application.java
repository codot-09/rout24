package com.example.rout24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Rout24Application {

	public static void main(String[] args) {
		SpringApplication.run(Rout24Application.class, args);
	}

}
