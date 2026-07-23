package com.grepo.grepobackend.repository;

import com.grepo.grepobackend.model.GitRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GitRepositoryRepository extends JpaRepository<GitRepository, Long> {

    List<GitRepository> findByGithubUsername(String githubUsername);

    Optional<GitRepository> findByGithubUsernameAndRepoName(String githubUsername, String repoName);
}