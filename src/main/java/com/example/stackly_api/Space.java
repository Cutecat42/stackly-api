package com.example.stackly_api;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Space {
    @Id
    @Column(
            name = "space_name",
            unique = true,
            nullable = false
    )
    private String spaceName;

    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL)
    private List<Stack> stacks = new ArrayList<>();

    public Space() {}

    public Space(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getSpaceName() {
        return this.spaceName;
    }

    @Override
    public String toString() {
        return "Space: " + spaceName;
    }
}
