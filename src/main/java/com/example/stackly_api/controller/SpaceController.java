package com.example.stackly_api.controller;

import com.example.stackly_api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SpaceController {
    private final SpaceRepository spaceRepository;
    private final StackRepository stackRepository;
    private final DocumentRepository documentRepository;

    public SpaceController(SpaceRepository spaceRepository,
                           StackRepository stackRepository,
                           DocumentRepository documentRepository) {
        this.spaceRepository = spaceRepository;
        this.stackRepository = stackRepository;
        this.documentRepository = documentRepository;
    }

    @PostMapping("/space")
    public ResponseEntity<Space> createSpace(@RequestBody SpaceRequest spaceRequest) {

        if (spaceRequest.spaceName == null || spaceRequest.spaceName.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        if (spaceRepository.existsById(spaceRequest.spaceName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Space space = new Space(spaceRequest.spaceName);
        Space saved = spaceRepository.save(space);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/stack")
    public ResponseEntity<Stack> createStack(@RequestBody StackRequest stackRequest) {

        if ((stackRequest.spaceName == null || stackRequest.spaceName.isBlank()) ||
                (stackRequest.stackName == null || stackRequest.stackName.isBlank()) ||
                (stackRequest.fieldSchema == null) || (stackRequest.fieldSchema.isEmpty())) {
            return ResponseEntity.badRequest().build();
        }

        if (stackRepository.existsById(stackRequest.stackName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        if (!spaceRepository.existsById(stackRequest.spaceName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Space space = spaceRepository.findById(stackRequest.spaceName).get();
        Stack stack = new Stack(space, stackRequest.stackName, stackRequest.fieldSchema);
        Stack saved = stackRepository.save(stack);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/document")
    public ResponseEntity<?> createDocument(@RequestBody DocumentRequest documentRequest) {

        if ((documentRequest.stackName == null || documentRequest.stackName.isBlank()) ||
                (documentRequest.customData == null) || (documentRequest.customData.isEmpty())) {
            return ResponseEntity.badRequest().build();
        }

        if (!stackRepository.existsById(documentRequest.stackName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Stack stack = stackRepository.findById(documentRequest.stackName).get();
        Map<String, Object> allowed = stack.getFieldSchema();

        for (String key : documentRequest.customData.keySet()) {
            if (!allowed.containsKey(key)) {
                String message = "Field '" + key + "' not allowed for stack " + stack.getStackName();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        }

        Document document = new Document(stack, documentRequest.customData);
        Document saved = documentRepository.save(document);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

}

