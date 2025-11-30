package com.example.stackly_api.repository;

import com.example.stackly_api.model.Document;
import com.example.stackly_api.model.Stack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByStack_stackNameIgnoreCase(String stackName);
    List<Document> findByinQueueTrue();
}
