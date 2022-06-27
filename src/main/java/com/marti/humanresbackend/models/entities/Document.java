package com.marti.humanresbackend.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "documents")
@Getter
@Setter
@Entity
@NoArgsConstructor

public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] document;

    private long workleaveId;

    public Document(byte[] document, long workleaveId) {
        this.document = document;
        this.workleaveId = workleaveId;
    }
}
