package com.example.stackly_api.dto;

import java.util.HashMap;

public class DocumentRequest {
    private String spaceName;
    private String stackName;
    private HashMap<String,Object> customData;
    private String filePath;
    private String fileName;
    private Long fileSize;
    private Boolean inQueue;

    public DocumentRequest() {};

    public DocumentRequest(String spaceName,
                           String stackName,
                           String fileName,
                           HashMap<String,Object> customData) {
        this.spaceName = spaceName;
        this.stackName = stackName;
        this.fileName = fileName;
        this.customData = customData;
    };

    public DocumentRequest(HashMap<String, Object> customData,
                    String filePath,
                    String fileName,
                    Long fileSize) {
        this.spaceName = "";
        this.stackName = "";
        this.customData = customData;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.inQueue = true;
    }

    public String getStackName() {
        return stackName;
    }

    public HashMap<String, Object> getCustomData() {
        return customData;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getSpaceName() { return spaceName; }
}
