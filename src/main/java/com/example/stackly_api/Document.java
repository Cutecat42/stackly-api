package com.example.stackly_api;

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
    private Stack stackName;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> customData = new HashMap<>();

    public Document() {};

    public Document(Stack stackName, HashMap<String, Object> customData) {
    this.stackName = stackName;
    this.customData = customData;
    }
}
