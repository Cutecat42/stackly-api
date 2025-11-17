package com.example.stackly_api.service;

import com.example.stackly_api.dto.DocumentRequest;
import com.example.stackly_api.model.Document;

public interface DocumentService {
    Document createDocument(DocumentRequest documentRequest);
}
