package com.example.stackly_api.dto;

import java.util.HashMap;

public class DocumentRequest {
    private String stackName;
    private HashMap<String,Object> customData;

    public DocumentRequest() {};

    public DocumentRequest(String stackName, HashMap<String,Object> customData) {
        this.stackName = stackName;
        this.customData = customData;
    };

    public String getStackName() {
        return stackName;
    }

    public HashMap<String, Object> getCustomData() {
        return customData;
    }
}
