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
import java.util.NoSuchElementException;

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

    @GetMapping("/spaces")
    public ResponseEntity<List<Space>> getAllSpaces() {
        List<Space> spaces = spaceService.getAllSpaces();
        return ResponseEntity.ok(spaces);
    }

    @GetMapping("/{space}/stacks")
    public ResponseEntity<List<Stack>> getAllStacksPerSpace(@PathVariable("space") String spaceName) {
        List<Stack> stacks = stackService.getAllStacksPerSpace(spaceName);
        return ResponseEntity.ok(stacks);
    }

    @GetMapping("/stack/{stackName}")
    public ResponseEntity<Stack> getStackByName(@PathVariable String stackName) {

        Stack stack = stackService.getStackByStackName(stackName)
                .orElseThrow(() -> new NoSuchElementException("Stack not found with name: " + stackName));

        return ResponseEntity.ok(stack);
    }

    @GetMapping("/stacks")
    public ResponseEntity<List<String>> getAllStackNamesForValidation() {
        List<String> stackNames = stackService.getAllStackNames();
        return ResponseEntity.ok(stackNames);
    }

    @GetMapping("/stack/{stack}/documents")
    public ResponseEntity<List<Document>> getAllDocumentsPerStack(@PathVariable("stack") String stackName) {
        List<Document> documents = documentService.getAllDocumentsPerStack(stackName);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/documents/{documentNumber}")
    public ResponseEntity<Document> getDocumentByNumber(@PathVariable Long documentNumber) {
        Document document = documentService.getDocumentByDocumentNumber(documentNumber)
                .orElseThrow(() -> new NoSuchElementException("Document not found with ID: " + documentNumber));

        return ResponseEntity.ok(document);
    }

    @PostMapping("/space")
    public ResponseEntity<Space> createSpace(@RequestBody SpaceRequest spaceRequest) {
        Space savedSpace = spaceService.createSpace(spaceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSpace);
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

