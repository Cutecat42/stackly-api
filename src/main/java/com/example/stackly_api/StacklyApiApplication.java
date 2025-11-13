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
    public CommandLineRunner commandLineRunner(SpaceRepository spaceRepository, StackRepository stackRepository) {
        return args -> {
            // Create and save a Space
            Space space = new Space("HR");
            if (!spaceRepository.existsById("HR")) {
                spaceRepository.save(space);
            }

            // Create and save a Stack in that Space
            Stack stack = new Stack(space,"Contracts");
            stackRepository.save(stack);

            System.out.println("✅ Space and Stack saved successfully!");
        };
    }
}

