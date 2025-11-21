package com.example.stackly_api.service;

import com.example.stackly_api.dto.SpaceRequest;
import com.example.stackly_api.model.Space;

import java.util.List;

public interface SpaceService {
    Space createSpace(SpaceRequest spaceRequest);
    List<Space> getAllSpaces();
    Space getSpaceByName(String spaceName);
}
