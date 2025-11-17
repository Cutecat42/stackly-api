package com.example.stackly_api.service;

import com.example.stackly_api.dto.StackRequest;
import com.example.stackly_api.model.Stack;

public interface StackService {
    Stack createStack(StackRequest stackRequest);
}
