package com.peccio.solar_system_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SolarSystemApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolarSystemApiApplication.class, args);
	}

}
