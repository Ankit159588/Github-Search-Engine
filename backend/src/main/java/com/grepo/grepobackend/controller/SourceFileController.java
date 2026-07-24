package com.grepo.grepobackend.controller;

import com.grepo.grepobackend.model.SourceFile;
import com.grepo.grepobackend.repository.SourceFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
public class SourceFileController {

    private final SourceFileRepository sourceFileRepository;

    @GetMapping("/api/sourcefile/{id}")
    public ResponseEntity<String> getFileContents(@PathVariable Long id) throws IOException {
        SourceFile sourceFile = sourceFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SourceFile not found: " + id));

        String username = sourceFile.getRepository().getGithubUsername();
        String repoName = sourceFile.getRepository().getRepoName();
        String filePath = sourceFile.getFilePath();

        Path fullPath = Path.of("cloned-repos", username, repoName, filePath);

        String contents = Files.readString(fullPath);

        return ResponseEntity.ok(contents);
    }
}