package com.example.stackly_api.service;

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
import java.text.SimpleDateFormat;
import java.text.ParseException;


@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;
    private final StackRepository stackRepository;

    public DocumentServiceImpl(StackRepository stackRepository, DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
        this.stackRepository = stackRepository;
    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
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

        for (Map.Entry<String, Object> entry : documentRequest.getCustomData().entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            String expectedType = (String) allowed.get(fieldName);

            if (fieldValue == null) {
                continue;
            }

            String valueStr = fieldValue.toString();

            switch (expectedType.toUpperCase()) {
                case "DATE":
                    if (!isValidDate(valueStr)) {
                        throw new IllegalArgumentException(
                                "Field '" + fieldName + "' must be a valid date in MM-dd-yyyy format. Found: " + valueStr);
                    }
                    break;
                case "NUMBER":
                    try {
                        Double.parseDouble(valueStr);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(
                                "Field '" + fieldName + "' must be a valid number. Found: " + valueStr);
                    }
                    break;
                case "STRING":
                    break;
                default:
                    logger.warn("Unknown field type '{}' for field '{}'. Skipping type validation.", expectedType, fieldName);
                    break;
            }
        }

        Document document = new Document(stack, documentRequest.getCustomData());
        return documentRepository.save(document);
    }

    @Override
    public List<Document> getAllDocumentsPerStack(String stackName) {
        return documentRepository.findByStack_stackNameIgnoreCase(stackName);
    }

    @Override
    public Optional<Document> getDocumentByDocumentNumber(Long documentNumber) {
        return documentRepository.findById(documentNumber);
    }
}