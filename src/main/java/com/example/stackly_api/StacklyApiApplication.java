package com.example.stackly_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StacklyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StacklyApiApplication.class, args);
	}

    @Bean
    public CommandLineRunner commandLineRunner(SpaceRepository SpaceRepository) {
        return args -> {
            Space joeContract = new Space("HR", "Employee Contracts",
                    "Joe");
            SpaceRepository.save(joeContract);

            Space invoiceDocument = new Space("HR", "Recruitment",
                    "ABC Company", 12345);
            SpaceRepository.save(invoiceDocument);
        };
    }
}

