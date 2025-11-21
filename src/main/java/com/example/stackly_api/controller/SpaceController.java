package com.example.stackly_api.controller;

import com.example.stackly_api.dto.DocumentRequest;
import com.example.stackly_api.dto.SpaceRequest;
import com.example.stackly_api.dto.StackRequest;
import com.example.stackly_api.model.Document;
import com.example.stackly_api.model.Space;
import com.example.stackly_api.model.Stack;
import com.example.stackly_api.service.DocumentService;
import com.example.stackly_api.service.SpaceService;
import com.example.stackly_api.service.StackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SpaceController {
    private final SpaceService spaceService;
    private final StackService stackService;
    private final DocumentService documentService;

    public SpaceController(SpaceService spaceService,
                           StackService stackService,
                           DocumentService documentService) {
        this.spaceService = spaceService;
        this.stackService = stackService;
        this.documentService = documentService;
    }

    @PostMapping("/space")
    public ResponseEntity<Space> createSpace(@RequestBody SpaceRequest spaceRequest) {
            Space savedSpace = spaceService.createSpace(spaceRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSpace);
    }

    @GetMapping("/spaces")
    public ResponseEntity<List<Space>> getAllSpaces() {
        List<Space> spaces = spaceService.getAllSpaces();
        return ResponseEntity.ok(spaces);
    }

    @GetMapping("/{spaceName}")
    public ResponseEntity<Space> getSpaceByName(@PathVariable String spaceName) {
        Space space = spaceService.getSpaceByName(spaceName);
        return ResponseEntity.ok(space);
    }

    @PostMapping("/stack")
    public ResponseEntity<Stack> createStack(@RequestBody StackRequest stackRequest) {
        Stack savedStack = stackService.createStack(stackRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStack);
    }

    @PostMapping("/document")
    public ResponseEntity<?> createDocument(@RequestBody DocumentRequest documentRequest) {

        Document savedDocument = documentService.createDocument(documentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDocument);
    }

}

