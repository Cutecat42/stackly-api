package com.example.stackly_api;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
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

    public Stack() {}

    public Stack(Space spaceName, String stackName) {
        this.spaceName = spaceName;
        this.stackName = stackName;
    }

    @Override
    public String toString() {
        return "Stack: " + stackName +
                "Space: " + spaceName;
    }
}
