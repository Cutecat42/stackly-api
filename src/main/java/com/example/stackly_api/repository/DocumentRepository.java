package com.example.stackly_api.repository;

import com.example.stackly_api.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
