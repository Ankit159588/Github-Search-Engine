package com.grepo.grepobackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubRepoDto {
    public String name;
    public String full_name;
    public String clone_url;
    public String default_branch;
}

//name — just the repo name, e.g. "linux"
//full_name — "torvalds/linux" (username/repo)
//clone_url — the HTTPS URL JGit will use to clone it
//default_branch — e.g. "master" or "main" — we'll need this later for cloning the right branch