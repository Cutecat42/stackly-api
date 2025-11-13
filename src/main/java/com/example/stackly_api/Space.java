package com.example.stackly_api;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
public class Space {
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

    @Column(
            name = "space_name",
            nullable = false
    )
    private final String spaceName;

    @Column(
            name = "stack_name",
            nullable = false
    )
    private final String stackName;

    @Column(
            name = "first_name"
    )
    private String firstName;

    @Column(
            name = "invoice_number"
    )
    private Integer invoiceNumber;

    //Stack: Employee Contracts
    public Space(String spaceName, String stackName, String firstName) {
        this.spaceName = spaceName;
        this.stackName = stackName;
        this.firstName = firstName;
    }

    //Stack: Recruitment
    public Space(String spaceName, String stackName,
                 String firstName, Integer invoiceNumber) {
        this(spaceName, stackName, firstName);
        this.invoiceNumber = invoiceNumber;
    }

    @Override
    public String toString() {
        return "Space: " + spaceName +
                ", Stack: " + stackName +
                ", Document #: " + documentNumber +
                ", AccountOnly: " + invoiceNumber;
    }
}
