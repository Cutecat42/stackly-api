package com.example.stackly_api.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class)
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
    private Space space;

    @OneToMany(mappedBy = "stack", cascade = CascadeType.ALL)
    private List<Document> documents = new ArrayList<>();

    @Type(type = "jsonb")
    @Column(
            name = "field_schema",
            columnDefinition = "jsonb",
            nullable = false
    )
    private HashMap<String, Object> fieldSchema = new HashMap<>();

    public Stack() {}

    public Stack(Space space, String stackName, HashMap<String, Object> fieldSchema) {
        this.space = space;
        this.stackName = stackName;
        this.fieldSchema = fieldSchema;
    }

    public String getStackName() {
        return stackName;
    }

    public String getSpaceName() {
        return space.getSpaceName();
    }

    public Map<String, Object> getFieldSchema() {
        return fieldSchema;
    }

    @Override
    public String toString() {
        return "Stack: " + stackName +
                "Space: " + space;
    }
}
