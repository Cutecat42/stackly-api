package com.example.stackly_api.service;

import com.example.stackly_api.dto.StackRequest;
import com.example.stackly_api.model.Space;
import com.example.stackly_api.model.Stack;

import java.util.List;
import java.util.Optional;

public interface StackService {
    Stack createStack(StackRequest stackRequest);
    List<Stack> getAllStacksPerSpace(String spaceName);
    Optional<Stack> getStackByStackName(String stackName);
}
