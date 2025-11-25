package com.example.stackly_api.repository;

import com.example.stackly_api.model.Space;
import com.example.stackly_api.model.Stack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StackRepository extends JpaRepository<Stack, String> {
    List<Stack> findBySpace_spaceName(String spaceName);
    Optional<Stack> findByStackName(String stackName);
    @Query("SELECT s.stackName FROM Stack s")
    List<String> findAllStackNames();
}
