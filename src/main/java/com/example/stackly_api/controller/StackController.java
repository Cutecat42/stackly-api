package com.example.stackly_api.controller;

import com.example.stackly_api.dto.StackRequest;
import com.example.stackly_api.model.Stack;
import com.example.stackly_api.service.StackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class StackController {
    private final StackService stackService;

    public StackController(StackService stackService) {
        this.stackService = stackService;
    }

    @GetMapping("/{space}/stacks")
    public ResponseEntity<List<Stack>> getAllStacksPerSpace(@PathVariable("space") String spaceName) {
        List<Stack> stacks = stackService.getAllStacksPerSpace(spaceName);
        return ResponseEntity.ok(stacks);
    }

    @GetMapping("/stack/{stackName}")
    public ResponseEntity<Stack> getStackByName(@PathVariable String stackName) {

        Stack stack = stackService.getStackByStackName(stackName)
                .orElseThrow(() -> new NoSuchElementException("Stack not found with name: " + stackName));

        return ResponseEntity.ok(stack);
    }

    @GetMapping("/stacks")
    public ResponseEntity<List<String>> getAllStackNamesForValidation() {
        List<String> stackNames = stackService.getAllStackNames();
        return ResponseEntity.ok(stackNames);
    }

    @PostMapping("/stack")
    public ResponseEntity<Stack> createStack(@RequestBody StackRequest stackRequest) {
        Stack savedStack = stackService.createStack(stackRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStack);
    }

}
