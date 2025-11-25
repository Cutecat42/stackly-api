package com.example.stackly_api.service.impl;

import com.example.stackly_api.service.DocumentService;
import com.example.stackly_api.repository.DocumentRepository;
import com.example.stackly_api.repository.StackRepository;
import com.example.stackly_api.model.Document;
import com.example.stackly_api.model.Stack;
import com.example.stackly_api.dto.DocumentRequest;
import com.example.stackly_api.exception.StackNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;
    private final StackRepository stackRepository;

    public DocumentServiceImpl(StackRepository stackRepository, DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
        this.stackRepository = stackRepository;
    }

    @Override
    public Document createDocument(DocumentRequest documentRequest) {
        if (documentRequest == null) {
            throw new IllegalArgumentException("Document request cannot be null.");
        }

        if (documentRequest.getStackName() == null || documentRequest.getStackName().isBlank() ||
                documentRequest.getCustomData() == null || documentRequest.getCustomData().isEmpty()) {
            throw new IllegalArgumentException("Stack/CustomRepository Data Fields cannot be blank.");
        }

        Stack stack = stackRepository.findById(documentRequest.getStackName()).
                orElseThrow(() -> new StackNotFoundException("Stack not found with name: "
                        + documentRequest.getStackName()));

        Map<String, Object> allowed = stack.getFieldSchema();

        for (String key : documentRequest.getCustomData().keySet()) {
            if (!allowed.containsKey(key)) {
                String message = "Field '" + key + "' not allowed for stack " + stack.getStackName();
                throw new IllegalArgumentException(message);
            }
        }

        Document document = new Document(stack, documentRequest.getCustomData());
        return documentRepository.save(document);
    }

    @Override
    public List<Document> getAllDocumentsPerStack(String stackName) {
        // --- LOGGING TRACE ADDED ---
        logger.info("--- DOCUMENT FETCH TRACE ---");
        logger.info("Attempting to fetch documents for stack: {}", stackName);

        // Execute the query using the IgnoreCase method
        List<Document> documents = documentRepository.findByStack_stackNameIgnoreCase(stackName);

        // Log the result size
        logger.info("Query returned {} documents for stack: {}", documents.size(), stackName);
        logger.info("--- END TRACE ---");
        // --- LOGGING TRACE ADDED ---

        return documents;
    }

    @Override
    public Optional<Document> getDocumentByDocumentNumber(Long documentNumber) {
        return documentRepository.findById(documentNumber);
    }
}