package com.example.stackly_api.service;

import com.example.stackly_api.dto.DocumentRequest;
import com.example.stackly_api.exception.DocumentNotFoundException;
import com.example.stackly_api.exception.SpaceNotFoundException;
import com.example.stackly_api.exception.StackNotFoundException;
import com.example.stackly_api.model.Document;
import com.example.stackly_api.model.Space;
import com.example.stackly_api.model.Stack;
import com.example.stackly_api.repository.DocumentRepository;
import com.example.stackly_api.repository.SpaceRepository;
import com.example.stackly_api.repository.StackRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    @Value("${stackly.upload.dir}")
    private String uploadDir;
    @Value("${stackly.change.dir}")
    private String changeDir;

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private final SpaceRepository spaceRepository;
    private final StackRepository stackRepository;
    private final DocumentRepository documentRepository;

    public FileStorageServiceImpl(SpaceRepository spaceRepository,
                                  StackRepository stackRepository,
                                  DocumentRepository documentRepository) {
        this.spaceRepository = spaceRepository;
        this.stackRepository = stackRepository;
        this.documentRepository = documentRepository;
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
    public Document saveDocumentToQueue(MultipartFile file, String documentName) throws IOException {

        String originalFileName = file.getOriginalFilename();
        String systemFileName = UUID.randomUUID().toString() + "_" + originalFileName;

        Path queueDir = Paths.get(uploadDir, "Queue");
        Path targetLocation = queueDir.resolve(systemFileName);

        if (!Files.exists(queueDir)) {
            Files.createDirectories(queueDir);
        }

        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        Document document = new Document();
        document.setFileName(documentName);
        document.setSystemFileName(systemFileName);
        document.setFilePath(targetLocation.toString());
        document.setFileSize(file.getSize());
        document.setInQueue(true);

        return document;
    }

    @Override
    public Document saveDocumentToSpace(DocumentRequest documentRequest) throws IOException  {
        if (documentRequest == null) {
            throw new IllegalArgumentException("Document request cannot be null.");
        }
        if (documentRequest.getStackName() == null || documentRequest.getStackName().isBlank() ||
                documentRequest.getSpaceName() == null || documentRequest.getSpaceName().isBlank() ||
                documentRequest.getCustomData() == null || documentRequest.getCustomData().isEmpty()) {
            throw new IllegalArgumentException("Space/Stack/Custom Data Fields cannot be blank.");
        }

        Space space = spaceRepository.findById(documentRequest.getSpaceName()).
                orElseThrow(() -> new SpaceNotFoundException("Space not found with name: "
                        + documentRequest.getSpaceName()));


        Stack stack = stackRepository.findById(documentRequest.getStackName()).
                orElseThrow(() -> new StackNotFoundException("Stack not found with name: "
                        + documentRequest.getStackName()));

        Map<String, Object> allowed = stack.getFieldSchema();

        Document document = documentRepository.findByfileName(documentRequest.getFileName()).
                orElseThrow(() -> new DocumentNotFoundException("Document not found with name: "
                        + documentRequest.getFileName()));

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

        Path sourcePath = Paths.get(document.getFilePath());
        String newFileName = document.getDocumentNumber() + "_" + document.getFileName();
        String stackPathName = documentRequest.getSpaceName() + "/" + documentRequest.getStackName();
        Path destinationDir = Paths.get(uploadDir, stackPathName);

        Path targetLocation = destinationDir.resolve(newFileName);

        if (!Files.exists(destinationDir)) {
            Files.createDirectories(destinationDir);
            logger.info("Created new directory path: {}", destinationDir);
        }

        logger.info("Moving file from {} to {}", sourcePath, targetLocation);
        Files.move(sourcePath, targetLocation, StandardCopyOption.REPLACE_EXISTING);

        document.setStack(stack);
        document.setSpace(space);
        document.setCustomData(documentRequest.getCustomData());
        document.setInQueue(false);
        document.setFilePath(targetLocation.toString());
        document.setFileName(newFileName);

        return documentRepository.save(document);
    }
}
