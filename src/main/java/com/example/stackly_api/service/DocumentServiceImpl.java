package com.example.stackly_api.service;

import com.example.stackly_api.dto.DocumentRequest;
import com.example.stackly_api.exception.StackNotFoundException;
import com.example.stackly_api.model.Document;
import com.example.stackly_api.model.Stack;
import com.example.stackly_api.repository.DocumentRepository;
import com.example.stackly_api.repository.StackRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DocumentServiceImpl implements DocumentService{
    private final DocumentRepository documentRepository;
    private final StackRepository stackRepository;

    public DocumentServiceImpl(StackRepository stackRepository, DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
        this.stackRepository = stackRepository;
    }

    @Override
    public Document createDocument(DocumentRequest documentRequest) {
        if ((documentRequest.stackName == null || documentRequest.stackName.isBlank()) ||
                (documentRequest.customData == null) || (documentRequest.customData.isEmpty())) {
            throw new IllegalArgumentException("Stack/CustomRepository Data Fields cannot be blank.");
        }

        Stack stack = stackRepository.findById(documentRequest.stackName).
                orElseThrow(() -> new StackNotFoundException("Stack not found with name: "
                + documentRequest.stackName));

        Map<String, Object> allowed = stack.getFieldSchema();

        for (String key : documentRequest.customData.keySet()) {
            if (!allowed.containsKey(key)) {
                String message = "Field '" + key + "' not allowed for stack " + stack.getStackName();
                throw new IllegalArgumentException(message);
            }
        }

        Document document = new Document(stack, documentRequest.customData);
        return documentRepository.save(document);
    }
}
