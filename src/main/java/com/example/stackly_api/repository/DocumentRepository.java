package com.example.stackly_api.repository;

import com.example.stackly_api.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByStack_stackNameIgnoreCase(String stackName);
    List<Document> findByinQueueTrue();
    Optional<Document> findByfileName(String fileName);
}
