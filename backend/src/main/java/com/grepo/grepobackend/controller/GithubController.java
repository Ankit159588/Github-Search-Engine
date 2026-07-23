package com.grepo.grepobackend.controller;

import com.grepo.grepobackend.model.GitRepository;
import com.grepo.grepobackend.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GithubController {

    private final GithubService githubService;

    @GetMapping("/api/index/{username}")
    public List<GitRepository> indexUser(@PathVariable String username) {
        return githubService.fetchAndSaveUserRepos(username);
    }
}