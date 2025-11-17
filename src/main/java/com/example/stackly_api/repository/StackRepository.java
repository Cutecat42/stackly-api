package com.example.stackly_api.repository;

import com.example.stackly_api.model.Stack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StackRepository extends JpaRepository<Stack, String> {
}
