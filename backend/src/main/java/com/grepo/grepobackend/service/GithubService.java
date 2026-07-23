package com.grepo.grepobackend.service;

import com.grepo.grepobackend.dto.GithubRepoDto;
import com.grepo.grepobackend.model.GitRepository;
import com.grepo.grepobackend.repository.GitRepositoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
public class GithubService {
    private final RestClient restClient = RestClient.create("https://api.github.com");

    @Autowired
    private GitRepositoryRepository gitRepositoryRepository;

    public List<GitRepository> fetchAndSaveUserRepos(String username) {
        GithubRepoDto[] repos = restClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .body(GithubRepoDto[].class);

//.get() — HTTP GET request.
//.uri("/users/{username}/repos", username) — {username} is a placeholder, filled in with the second argument. Builds the full URL: https://api.github.com/users/torvalds/repos.
//.retrieve() — actually sends the request.
//.body(GithubRepoDto[].class) — tells Jackson "parse the JSON response body as an array of GithubRepoDto" (GitHub returns a JSON array [ {...}, {...} ] for this endpoint, hence array not single object).

        return List.of(repos).stream()
                .map(dto -> saveOrUpdate(username, dto))
                .toList();
    }

    private GitRepository saveOrUpdate(String username, GithubRepoDto dto) {
        GitRepository repo = gitRepositoryRepository
                .findByGithubUsernameAndRepoName(username, dto.name)
                .orElse(new GitRepository());

        repo.setGithubUsername(username);
        repo.setRepoName(dto.name);
        repo.setCloneUrl(dto.clone_url);

        return gitRepositoryRepository.save(repo);
    }

}
