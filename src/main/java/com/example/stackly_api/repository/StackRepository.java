package com.example.stackly_api.repository;

import com.example.stackly_api.model.Space;
import com.example.stackly_api.model.Stack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StackRepository extends JpaRepository<Stack, String> {
    List<Stack> findBySpace_spaceName(String spaceName);
}
