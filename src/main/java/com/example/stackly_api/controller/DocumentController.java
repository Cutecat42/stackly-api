package com.example.stackly_api.controller;

import com.example.stackly_api.dto.DocumentRequest;
import com.example.stackly_api.model.Document;
import com.example.stackly_api.service.DocumentService;
import com.example.stackly_api.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class DocumentController {
    private final DocumentService documentService;
    private final FileStorageService fileStorageService;

    public DocumentController(DocumentService documentService, FileStorageService fileStorageService) {
        this.documentService = documentService;
        this.fileStorageService = fileStorageService;
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

    @GetMapping("/inQueue")
    public ResponseEntity<List<Document>> getAllDocumentsInQueue(@RequestBody DocumentRequest documentRequest) {
        List<Document> documents = documentService.getAllDocumentsInQueue();
        return ResponseEntity.ok(documents);
    }

    @PostMapping("/document/upload")
    public ResponseEntity<Document> uploadDocument(
            @RequestPart("file") MultipartFile file,
            @RequestParam("documentName") String documentName
    ) {
        Document savedDocument = documentService.saveDocument(file, documentName);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDocument);
    }

    @PutMapping("/document/assign")
    public ResponseEntity<Document> assignDocumentToSpace(@RequestBody DocumentRequest documentRequest) throws IOException, IOException {

        Document assignedDocument = fileStorageService.saveDocumentToSpace(documentRequest);

        return new ResponseEntity<>(assignedDocument, HttpStatus.OK);
    }
}
