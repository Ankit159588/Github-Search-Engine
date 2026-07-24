package com.grepo.grepobackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "source_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SourceFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "repository_id")
    private GitRepository repository;

    private String filePath;
    private String packageName;
    private Long sourceFileId;
}
