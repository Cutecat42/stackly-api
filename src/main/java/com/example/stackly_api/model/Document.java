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
            nullable = false
    )
    private Stack stack;

    @Type(type = "jsonb")
    @Column(
            columnDefinition = "jsonb",
            nullable = false)
    private Map<String, Object> customData = new HashMap<>();

    public Document() {};

    public Document(Stack stack, HashMap<String, Object> customData) {
    this.stack = stack;
    this.customData = customData;
    }

    public String getStackName() {
        return stack.getStackName();
    }

    public String getCustomData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(customData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
