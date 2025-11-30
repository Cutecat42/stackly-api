package com.example.stackly_api.controller;

import com.example.stackly_api.dto.SpaceRequest;
import com.example.stackly_api.model.Space;
import com.example.stackly_api.service.SpaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SpaceController {
    private final SpaceService spaceService;

    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @GetMapping("/spaces")
    public ResponseEntity<List<Space>> getAllSpaces() {
        List<Space> spaces = spaceService.getAllSpaces();
        return ResponseEntity.ok(spaces);
    }

    @PostMapping("/space")
    public ResponseEntity<Space> createSpace(@RequestBody SpaceRequest spaceRequest) {
        Space savedSpace = spaceService.createSpace(spaceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSpace);
    }

}

