package com.example.stackly_api.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.util.HashMap;
import java.util.Map;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
public class Document {
    @Id
    @SequenceGenerator(
            name = "space_sequence",
            sequenceName = "space_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "space_sequence"
    )
    @Column(
            name = "document_number",
            unique = true,
            updatable = false
    )
    private long documentNumber;

    @ManyToOne
    @JoinColumn(
            name = "stack_name",
            referencedColumnName = "stack_name"
    )
    private Stack stack;

    @ManyToOne
    @JoinColumn(
            name = "space_name",
            referencedColumnName = "space_name"
    )
    private Space space;

    @Column(
            name = "file_path",
            unique = true
    )
    private String filePath;

    @Column(
            name = "file_name"
    )
    private String fileName;

    @Column(
            name = "file_size"
    )
    private long fileSize;

    @Column(
            name = "in_queue"
    )
    private boolean inQueue;

    @Type(type = "jsonb")
    @Column(
            columnDefinition = "jsonb",
            nullable = false)
    private Map<String, Object> customData = new HashMap<>();

    public Document() {};

//    public Document(Stack stack, HashMap<String, Object> customData) {
//    this.stack = stack;
//    this.customData = customData;
//    }

//    public Document(String filePath,
//                    String fileName,
//                    Long fileSize) {
//        this.space = null;
//        this.stack = null;
//        this.customData = null;
//        this.filePath = filePath;
//        this.fileName = fileName;
//        this.fileSize = fileSize;
//        this.inQueue = true;
//    }

    public String getStackName() {
        return stack != null ? stack.getStackName() : null;
    }

    public String getCustomData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(customData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public long getDocumentNumber() {
        return documentNumber;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public boolean isInQueue() {
        return inQueue;
    }

    public Space getSpace() {
        return space;
    }

    public Stack getStack() {
        return stack;
    }

    public void setFileName(String name) {
        this.fileName = name;
    }

    public void setFilePath(String path) {
        this.filePath = path;
    }

    public void setFileSize(Long size) {
        this.fileSize = size;
    }

    public void setInQueue(Boolean inQueue) {
        this.inQueue = inQueue;
    }
}
