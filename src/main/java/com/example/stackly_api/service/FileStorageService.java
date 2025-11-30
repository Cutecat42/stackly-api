package com.example.stackly_api.service;

import com.example.stackly_api.dto.DocumentRequest;
import com.example.stackly_api.model.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    Document saveDocumentToQueue(MultipartFile file, String documentName) throws IOException;
    Document saveDocumentToSpace(DocumentRequest documentRequest) throws IOException;
}
