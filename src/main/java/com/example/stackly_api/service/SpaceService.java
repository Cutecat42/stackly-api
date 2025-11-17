package com.example.stackly_api.service;

import com.example.stackly_api.dto.SpaceRequest;
import com.example.stackly_api.model.Space;

public interface SpaceService {
    Space createSpace(SpaceRequest spaceRequest);
}
