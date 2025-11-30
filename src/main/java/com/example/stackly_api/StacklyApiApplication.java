package com.example.stackly_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
public class StacklyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StacklyApiApplication.class, args);

        String folderPath = "/app/uploads";
        File folder = new File(folderPath);

        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                System.out.println("Folder created successfully: " + folderPath);
            } else {
                System.out.println("Failed to create folder: " + folderPath);
            }
        } else {
            System.out.println("Folder already exists: " + folderPath);
        }
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
        };
    }
}

