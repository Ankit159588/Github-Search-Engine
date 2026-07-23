package com.grepo.grepobackend.controller;

import com.grepo.grepobackend.model.GitRepository;
import com.grepo.grepobackend.repository.GitRepositoryRepository;
import com.grepo.grepobackend.service.CloneService;
import com.grepo.grepobackend.service.ParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CloneController {

    private final CloneService cloneService;
    private final ParserService parserService;
    private final GitRepositoryRepository gitRepositoryRepository;

    @GetMapping("/api/clone/{id}")
    public String cloneById(@PathVariable Long id) {
        Optional<GitRepository> repoOpt = gitRepositoryRepository.findById(id);

        if (repoOpt.isEmpty()) {
            return "No repository found with id " + id;
        }

        GitRepository repo = repoOpt.get();
//        .get() — unwraps the box, pulling the actual GitRepository object out of the Optional so we can use it normally. Think of Optional like a wrapper you have to "open" before using what's inside — .get() is the "open it" step. It's only safe to call .get() after confirming the box isn't empty (which is exactly why we checked .isEmpty() first — calling .get() on an empty Optional throws an exception).
        var destDir = cloneService.cloneRepo(repo);
//
//        return "Cloned to: " + destDir.getAbsolutePath();
        var javaFiles = parserService.findJavaFiles(destDir);
        for (File file : javaFiles) {
            parserService.parseAndSave(file, repo, destDir);
        }
        return "Cloned to: " + destDir.getAbsolutePath() + " | Found " + javaFiles.size() + " java files";
    }
}