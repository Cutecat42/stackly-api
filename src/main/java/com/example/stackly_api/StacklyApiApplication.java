package com.example.stackly_api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class StacklyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StacklyApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(SpaceRepository spaceRepository,
                                               StackRepository stackRepository,
                                               DocumentRepository documentRepository) {
        return args -> {
            // Create and save a Space
            Space space = new Space("HR");
            if (!spaceRepository.existsById("HR")) {
                spaceRepository.save(space);
            }

            HashMap<String, Object> fieldSchema = new HashMap<>();
            fieldSchema.put("employeeName", "String");
            fieldSchema.put("hireDate", "Date");
            fieldSchema.put("department", "String");

            // Create and save a Stack in that Space
            Stack stack = new Stack(space,"Contracts",fieldSchema);
            stackRepository.save(stack);
            System.out.println(stack.getFieldSchema());

            //Create Document in the newly created Stack
            HashMap<String, Object> docFields = new HashMap<>();
            docFields.put("employeeName", "John");
            docFields.put("notAllowed", "12-1-2025");


            HashMap<String, String> allowedFields = new ObjectMapper()
                    .readValue(stack.getFieldSchema(), new TypeReference<HashMap<String, String>>() {});

            for (String key : docFields.keySet()) {
                if (!allowedFields.containsKey(key)) {
                    throw new IllegalArgumentException("Field '" + key + "' not allowed for stack " + stack.getStackName());
                }
            }
            Document document = new Document(stack, docFields);
            documentRepository.save(document);
        };
    }
}

