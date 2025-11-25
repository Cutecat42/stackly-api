package com.example.stackly_api.service;

import com.example.stackly_api.dto.DocumentRequest;
import com.example.stackly_api.model.Document;
import com.example.stackly_api.model.Space;
import com.example.stackly_api.model.Stack;

import java.util.List;
import java.util.Optional;

public interface DocumentService {
    Document createDocument(DocumentRequest documentRequest);
    List<Document> getAllDocumentsPerStack(String stackName);
    Optional<Document> getDocumentByDocumentNumber(Long documentNumber);
}
