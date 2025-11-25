package com.example.stackly_api.service;

import com.example.stackly_api.dto.SpaceRequest;
import com.example.stackly_api.exception.SpaceNameConflictException;
import com.example.stackly_api.exception.SpaceNotFoundException;
import com.example.stackly_api.model.Space;
import com.example.stackly_api.repository.SpaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaceServiceImpl implements SpaceService {
    private final SpaceRepository spaceRepository;

    public SpaceServiceImpl(SpaceRepository spaceRepository){
        this.spaceRepository = spaceRepository;
    }

    @Override
    public Space createSpace(SpaceRequest spaceRequest) {

        if (spaceRequest == null) {
            throw new IllegalArgumentException("Space request cannot be null.");
        }

        if (spaceRequest.getSpaceName() == null || spaceRequest.getSpaceName().isBlank()) {
            throw new IllegalArgumentException("Space name cannot be null or blank.");
        }

        if (spaceRepository.existsById(spaceRequest.getSpaceName())) {
            throw new SpaceNameConflictException("Space already found with name: "
                    + spaceRequest.getSpaceName());
        }

        Space space = new Space(spaceRequest.getSpaceName());
        return spaceRepository.save(space);
    }

    @Override
    public List<Space> getAllSpaces() {
        return spaceRepository.findAll();
    }
}
