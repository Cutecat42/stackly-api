package com.example.stackly_api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Stack {
    @Id
    @Column(
            name = "stack_name",
            unique = true,
            nullable = false
    )
    private String stackName;

    @ManyToOne
    @JoinColumn(
            name = "space_name",
            nullable = false
    )
    private Space spaceName;

    @OneToMany(mappedBy = "stackName", cascade = CascadeType.ALL)
    private List<Document> documents = new ArrayList<>();

    @Type(type = "jsonb")
    @Column(
            name = "field_schema",
            columnDefinition = "jsonb"
    )
    private HashMap<String, Object> fieldSchema = new HashMap<>();

    public Stack() {}

    public Stack(Space spaceName, String stackName, HashMap<String, Object> fieldSchema) {
        this.spaceName = spaceName;
        this.stackName = stackName;
        this.fieldSchema = fieldSchema;
    }

    public String getStackName() {
        return stackName;
    }

    public String getFieldSchema() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(fieldSchema);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String toString() {
        return "Stack: " + stackName +
                "Space: " + spaceName;
    }
}
