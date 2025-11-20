package com.example.stackly_api.dto;

public class SpaceRequest {
    private String spaceName;

    public SpaceRequest(String spaceName) {
        this.spaceName = spaceName;
    }
    public String getSpaceName() {
        return spaceName;
    }
}
