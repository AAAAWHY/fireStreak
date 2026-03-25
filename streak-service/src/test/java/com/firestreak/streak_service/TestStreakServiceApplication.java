package com.firestreak.streak_service;

import org.springframework.boot.SpringApplication;

public class TestStreakServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(StreakServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
