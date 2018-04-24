package com.csci4050.cinema_system;

import com.csci4050.cinema_system.configuration.StorageProperties;
import com.csci4050.cinema_system.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class CinemaSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaSystemApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}

}
