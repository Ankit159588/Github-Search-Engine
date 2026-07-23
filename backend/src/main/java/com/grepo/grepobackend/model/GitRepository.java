package com.grepo.grepobackend.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "git_repository")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GitRepository {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String githubUsername;
    private String repoName;
    private String cloneUrl;
    private String lastCommitSha;

}
