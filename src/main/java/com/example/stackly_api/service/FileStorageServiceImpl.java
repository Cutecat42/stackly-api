package com.example.stackly_api.service;

import com.example.stackly_api.dto.DocumentRequest;
import com.example.stackly_api.model.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService{
    @Value("${stackly.upload.dir}")
    private String uploadDir;

    @Override
    public Document saveDocumentToQueue(MultipartFile file, String documentName) throws IOException {

        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;

        Path uploadPath = Paths.get(uploadDir);
        Path targetLocation = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);


        Document document = new Document();
        document.setFileName(documentName);
        document.setFilePath(targetLocation.toString());
        document.setFileSize(file.getSize());
        document.setInQueue(true);

        return document;
    }
}
