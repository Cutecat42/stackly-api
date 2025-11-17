package com.example.stackly_api.service;

import com.example.stackly_api.dto.SpaceRequest;
import com.example.stackly_api.exception.SpaceNameConflictException;
import com.example.stackly_api.model.Space;
import com.example.stackly_api.repository.SpaceRepository;
import org.springframework.stereotype.Service;

@Service
public class SpaceServiceImpl implements SpaceService {
    private final SpaceRepository spaceRepository;

    public SpaceServiceImpl(SpaceRepository spaceRepository){
        this.spaceRepository = spaceRepository;
    }

    @Override
    public Space createSpace(SpaceRequest spaceRequest) {

        if (spaceRequest.spaceName == null || spaceRequest.spaceName.isBlank()) {
            throw new IllegalArgumentException("Space name cannot be blank.");
        }

        if (spaceRepository.existsById(spaceRequest.spaceName)) {
            throw new SpaceNameConflictException("Space already found with name: "
                    + spaceRequest.spaceName);
        }

        Space space = new Space(spaceRequest.spaceName);
        return spaceRepository.save(space);
    }
}
