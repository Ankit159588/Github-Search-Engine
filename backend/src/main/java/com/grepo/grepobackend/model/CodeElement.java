package com.grepo.grepobackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "code_elements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeElement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_file_id")
    private SourceFile sourceFile;

    @Enumerated(EnumType.STRING)
    private ElementType type;

    private String name;
    private String signature;
    private int startLine;
    private int endLine;
}