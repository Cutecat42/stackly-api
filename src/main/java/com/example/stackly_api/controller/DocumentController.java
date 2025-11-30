package com.example.stackly_api.controller;

import com.example.stackly_api.dto.DocumentRequest;
import com.example.stackly_api.model.Document;
import com.example.stackly_api.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
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
}
