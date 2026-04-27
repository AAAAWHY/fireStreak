package com.firestreak.streak_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StreakServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreakServiceApplication.class, args);
	}

}