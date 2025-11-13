package com.example.stackly_api;

import javax.persistence.*;

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

    @Column(
            name = "first_name"
    )
    private String firstName;

    @Column(
            name = "invoice_number"
    )
    private Integer invoiceNumber;

    public Document() {};

    public Document(Stack stackName, String firstName) {

    }
}
