package com.example.stackly_api.dto;

import java.util.HashMap;

public class StackRequest {
    private String spaceName;
    private String stackName;
    private HashMap<String,Object> fieldSchema;

    public StackRequest(String spaceName, String stackName, HashMap<String, Object> fieldSchema) {
        this.spaceName = spaceName;
        this.stackName = stackName;
        this.fieldSchema = fieldSchema;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public String getStackName() {
        return stackName;
    }

    public HashMap<String, Object> getFieldSchema() {
        return fieldSchema;
    }
}
