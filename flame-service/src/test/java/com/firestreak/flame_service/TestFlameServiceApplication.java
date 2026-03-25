package com.firestreak.flame_service;

import org.springframework.boot.SpringApplication;

public class TestFlameServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(FlameServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
