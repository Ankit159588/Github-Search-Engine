package com.grepo.grepobackend.service;

import com.grepo.grepobackend.model.GitRepository;
import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CloneService {
    private static final String CLONE_BASE_DIR = "cloned-repos";

    public File cloneRepo(GitRepository repo) {
        Path destPath = Paths.get(CLONE_BASE_DIR, repo.getGithubUsername(), repo.getRepoName());
        File destDir = destPath.toFile();

        if (destDir.exists()) {
            deleteDirectory(destDir);
        }

        // cloning logic
        try{
            Git.cloneRepository()
                    .setURI(repo.getCloneUrl()) /** setURI(repo.getCloneUrl()) — the HTTPS URL to clone from, e.g. https://github.com/Ankit159588/Learn-Spring-Boot.git. **/
                    .setDirectory(destDir)
                    .setDepth(1)
                    .call()
                    .close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clone " + repo.getRepoName(), e);
        }

        return destDir;
    }

    private void deleteDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        dir.delete();
    }

}
